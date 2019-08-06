package io.taig.schelm

import cats.implicits._

class HtmlDiffer[A] extends Differ[Html[A], HtmlDiff[A]] {
  override def diff(previous: Html[A], next: Html[A]): Option[HtmlDiff[A]] =
    // format: off
    (previous.value, next.value) match {
      case (previous: Component.Element[Html[A], A], next: Component.Element[Html[A], A]) =>
        element(previous, next)
      case (previous: Component.Fragment[Html[A]], next: Component.Fragment[Html[A]]) =>
        fragment(previous, next)
      case (previous: Component.Lazy[Html[A]], next: Component.Lazy[Html[A]]) =>
        lzy(previous, next)
      case (previous: Component.Text, next: Component.Text) =>
        text(previous, next)
      case _ => HtmlDiff.Replace(next).some
    }
    // format: on

  def element(
      previous: Component.Element[Html[A], A],
      next: Component.Element[Html[A], A]
  ): Option[HtmlDiff[A]] = None

  def fragment(
      previous: Component.Fragment[Html[A]],
      next: Component.Fragment[Html[A]]
  ): Option[HtmlDiff[A]] = None

  def lzy(
      previous: Component.Lazy[Html[A]],
      next: Component.Lazy[Html[A]]
  ): Option[HtmlDiff[A]] =
    if (previous.hash != next.hash) diff(previous.eval.value, next.eval.value)
    else None

  def text(
      previous: Component.Text,
      next: Component.Text
  ): Option[HtmlDiff[A]] =
    if (previous.value != next.value) HtmlDiff.UpdateText(next.value).some
    else None
}

object HtmlDiffer {
  def apply[A]: Differ[Html[A], HtmlDiff[A]] = new HtmlDiffer[A]
}
