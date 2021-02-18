package io.taig.schelm.css.data

import scala.collection.mutable

import cats.Monoid
import io.taig.schelm.util.Text

final case class Declarations(values: List[Declaration]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def concat(declarations: Declarations, divider: String): Declarations = {
    val self = this.toMap.to(mutable.LinkedHashMap) // Order matters

    declarations.toMap.foreach { case (name, value) =>
      self.updateWith(name) {
        case Some(current) => Some(current.concat(value, divider))
        case None          => Some(value)
      }
    }

    Declarations.from(self.map { case (name, value) => Declaration(name, value) })
  }

  def :+(declaration: Declaration): Declarations = Declarations(values :+ declaration)

  def +:(declaration: Declaration): Declarations = Declarations(declaration +: values)

  def ++(declarations: Declarations): Declarations = Declarations(values ++ declarations.values)

  def toMap: Map[Declaration.Name, Declaration.Value] =
    values.map { case Declaration(name, value) => (name, value) }.toMap

  override def toString: String = if (isEmpty) "{}"
  else
    s"""{
       |  ${Text.align(2)(values.mkString(",\n"))}
       |}""".stripMargin
}

object Declarations {
  val Empty: Declarations = Declarations(List.empty)

  def from(declarations: Iterable[Declaration]): Declarations = Declarations(declarations.toList)

  def of(declarations: Declaration*): Declarations = from(declarations)

  def one(declaration: Declaration): Declarations = Declarations(List(declaration))

  implicit val monoid: Monoid[Declarations] = new Monoid[Declarations] {
    override def empty: Declarations = Empty

    override def combine(x: Declarations, y: Declarations): Declarations = x ++ y
  }
}
