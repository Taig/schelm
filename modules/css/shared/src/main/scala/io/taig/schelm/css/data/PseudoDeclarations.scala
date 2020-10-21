package io.taig.schelm.css.data

import scala.collection.mutable

import cats.Monoid

final case class PseudoDeclarations(values: List[PseudoDeclaration]) {
  def isEmpty: Boolean = values.isEmpty

  def concat(declarations: PseudoDeclarations, divider: String): PseudoDeclarations = {
    val self = this.toMap.to(mutable.HashMap)

    declarations.values.foreach {
      case PseudoDeclaration(modifier, declarations) =>
        self.updateWith(modifier) {
          case Some(current) => Some(current.concat(declarations, divider))
          case None => Some(declarations)
        }
    }

    PseudoDeclarations.from(self.map { case (modifier, declarations) => PseudoDeclaration(modifier, declarations) })
  }

  def ++(declarations: PseudoDeclarations): PseudoDeclarations = PseudoDeclarations(values ++ declarations.values)

  def :+(declaration: PseudoDeclaration): PseudoDeclarations = PseudoDeclarations(values :+ declaration)

  def toRules(selectors: Selectors): List[Rule.Block] = values.map(_.toRule(selectors))

  def toMap: Map[Modifier, Declarations] = values.map { case PseudoDeclaration(modifier, declarations) => (modifier, declarations) }.toMap
}

object PseudoDeclarations {
  val Empty: PseudoDeclarations = PseudoDeclarations(List.empty)

  def from(declarations: Iterable[PseudoDeclaration]): PseudoDeclarations = PseudoDeclarations(declarations.toList)

  implicit val monoid: Monoid[PseudoDeclarations] =
    new Monoid[PseudoDeclarations] {
      override def empty: PseudoDeclarations = Empty

      override def combine(x: PseudoDeclarations, y: PseudoDeclarations): PseudoDeclarations = x ++ y
    }
}
