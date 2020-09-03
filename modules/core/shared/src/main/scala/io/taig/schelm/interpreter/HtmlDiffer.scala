package io.taig.schelm.interpreter

import io.taig.schelm.algebra.Differ
import io.taig.schelm.data.{Html, HtmlDiff, Node, Text}
import cats.implicits._

final class HtmlDiffer[Event] extends Differ[Html[Event], HtmlDiff[Event]] {
  override def diff(previous: Html[Event], next: Html[Event]): Option[HtmlDiff[Event]] =
    (previous.node, next.node) match {
      case (previous: Text[Event], next: Text[Event]) => text(previous, next)
      case _                                          => HtmlDiff.Replace(next).some
    }

  def text(previous: Text[Event], next: Text[Event]): Option[HtmlDiff[Event]] =
    if (previous.value != next.value) HtmlDiff.UpdateText(next.value).some else none
}

object HtmlDiffer {
  def apply[Event]: Differ[Html[Event], HtmlDiff[Event]] = new HtmlDiffer[Event]
}
