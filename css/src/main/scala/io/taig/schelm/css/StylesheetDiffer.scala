package io.taig.schelm.css

import cats.implicits._
import io.taig.schelm._

final class StylesheetDiffer[A]
    extends Differ[StylesheetWidget[A], StylesheetDiff] {
  override def diff(
      previous: StylesheetWidget[A],
      next: StylesheetWidget[A]
  ): Option[StylesheetDiff] = {
    val stylesheet = compare(previous, next)
    if (stylesheet.isEmpty) None else StylesheetDiff(stylesheet).some
  }

  def compare(
      previous: StylesheetWidget[A],
      next: StylesheetWidget[A]
  ): Stylesheet = {
    val node = next.payload(unit)

    // format: off
    val children = (previous.component(unit), next.component(unit)) match {
      case (previous: Component.Element[StylesheetWidget[A], A], next: Component.Element[StylesheetWidget[A], A]) =>
        element(previous, next)
      case (previous: Component.Fragment[StylesheetWidget[A]], next: Component.Fragment[StylesheetWidget[A]]) =>
        fragment(previous, next)
      case (previous: Component.Lazy[StylesheetWidget[A]], next: Component.Lazy[StylesheetWidget[A]]) =>
        lzy(previous, next)
      case (_: Component.Text, _: Component.Text) => Stylesheet.Empty
      case _ => next.merge
    }
    // format: on

    node ++ children
  }

  def element(
      previous: Component.Element[StylesheetWidget[A], A],
      next: Component.Element[StylesheetWidget[A], A]
  ): Stylesheet = children(previous.children, next.children)

  def fragment(
      previous: Component.Fragment[StylesheetWidget[A]],
      next: Component.Fragment[StylesheetWidget[A]]
  ): Stylesheet = children(previous.children, next.children)

  def lzy(
      previous: Component.Lazy[StylesheetWidget[A]],
      next: Component.Lazy[StylesheetWidget[A]]
  ): Stylesheet =
    if (previous.hash == next.hash) Stylesheet.Empty
    else compare(previous.eval.value, next.eval.value)

  def children(
      previous: Children[StylesheetWidget[A]],
      next: Children[StylesheetWidget[A]]
  ): Stylesheet =
    // format: off
    (previous, next) match {
      case (previous: Children.Indexed[StylesheetWidget[A]], next: Children.Indexed[StylesheetWidget[A]]) =>
        children(previous, next)
      case (previous: Children.Identified[StylesheetWidget[A]], next: Children.Identified[StylesheetWidget[A]]) =>
        // TODO
        Stylesheet.Empty
      case _ => next.values.map(_.merge).combineAll
    }
    // format: on

  def children(
      previous: Children.Indexed[StylesheetWidget[A]],
      next: Children.Indexed[StylesheetWidget[A]]
  ): Stylesheet =
    if (next.isEmpty) Stylesheet.Empty
    else {
      val left = previous.values
      val right = next.values
      val comparisons = (left zip right).map(compare _ tupled).combineAll
      val additions = right.drop(left.length).map(_.merge).combineAll
      comparisons ++ additions
    }
}

object StylesheetDiffer {
  def apply[A]: Differ[StylesheetWidget[A], StylesheetDiff] =
    new StylesheetDiffer[A]
}
