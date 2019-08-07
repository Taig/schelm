package io.taig.schelm.css

import cats.implicits._
import io.taig.schelm._

final class StylesheetDiffer[A] extends Differ[StyledHtml[A], StylesheetDiff] {
  override def diff(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Option[StylesheetDiff] = {
    val stylesheet = compare(previous, next)
    if (stylesheet.isEmpty) None else StylesheetDiff(stylesheet).some
  }

  def compare(
      previous: StyledHtml[A],
      next: StyledHtml[A]
  ): Stylesheet = {
    val node = next.head

    // format: off
    val children = (previous.tail, next.tail) match {
      case (previous: Component.Element[StyledHtml[A], A], next: Component.Element[StyledHtml[A], A]) =>
        element(previous, next)
      case (previous: Component.Fragment[StyledHtml[A]], next: Component.Fragment[StyledHtml[A]]) =>
        fragment(previous, next)
      case (previous: Component.Lazy[StyledHtml[A]], next: Component.Lazy[StyledHtml[A]]) =>
        lzy(previous, next)
      case (_: Component.Text, _: Component.Text) => Stylesheet.Empty
      case _ => toStylesheet(next)
    }
    // format: on

    node ++ children
  }

  def element(
      previous: Component.Element[StyledHtml[A], A],
      next: Component.Element[StyledHtml[A], A]
  ): Stylesheet = children(previous.children, next.children)

  def fragment(
      previous: Component.Fragment[StyledHtml[A]],
      next: Component.Fragment[StyledHtml[A]]
  ): Stylesheet = children(previous.children, next.children)

  def lzy(
      previous: Component.Lazy[StyledHtml[A]],
      next: Component.Lazy[StyledHtml[A]]
  ): Stylesheet =
    if (previous.hash == next.hash) Stylesheet.Empty
    else compare(previous.eval.value, next.eval.value)

  def children(
      previous: Children[StyledHtml[A]],
      next: Children[StyledHtml[A]]
  ): Stylesheet =
    // format: off
    (previous, next) match {
      case (previous: Children.Indexed[StyledHtml[A]], next: Children.Indexed[StyledHtml[A]]) =>
        children(previous, next)
      case (previous: Children.Identified[StyledHtml[A]], next: Children.Identified[StyledHtml[A]]) =>
        // TODO
        Stylesheet.Empty
      case _ => next.values.map(toStylesheet).combineAll
    }
    // format: on

  def children(
      previous: Children.Indexed[StyledHtml[A]],
      next: Children.Indexed[StyledHtml[A]]
  ): Stylesheet =
    if (next.isEmpty) Stylesheet.Empty
    else {
      val left = previous.values
      val right = next.values
      val comparisons = (left zip right).map(compare _ tupled).combineAll
      val additions = right.drop(left.length).map(toStylesheet).combineAll
      comparisons ++ additions
    }
}

object StylesheetDiffer {
  def apply[A]: Differ[StyledHtml[A], StylesheetDiff] = new StylesheetDiffer[A]
}
