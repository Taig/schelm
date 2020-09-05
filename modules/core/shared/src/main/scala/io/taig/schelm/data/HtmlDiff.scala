package io.taig.schelm.data

sealed abstract class HtmlDiff[+Event] extends Product with Serializable

object HtmlDiff {
  final case class AddAttribute(attribute: Attribute) extends HtmlDiff[Nothing]
  final case class AppendChild[Event](html: Html[Event]) extends HtmlDiff[Event]
  final case class AddListener[Event](listener: Listener[Event]) extends HtmlDiff[Event]
  final case object Clear extends HtmlDiff[Nothing]
  final case class Group[A](diffs: List[HtmlDiff[A]]) extends HtmlDiff[A]
  final case class Replace[Event](html: Html[Event]) extends HtmlDiff[Event]
  final case class RemoveAttribute(key: String) extends HtmlDiff[Nothing]
  final case class RemoveChild(index: Int) extends HtmlDiff[Nothing]
  final case class RemoveListener(event: String) extends HtmlDiff[Nothing]
  final case class UpdateAttribute(key: String, value: Attribute.Value) extends HtmlDiff[Nothing]
  final case class UpdateChild[A](index: Int, diff: HtmlDiff[A]) extends HtmlDiff[A]
  final case class UpdateListener[A](event: String, action: Listener.Action[A]) extends HtmlDiff[A]
  final case class UpdateText(value: String) extends HtmlDiff[Nothing]
}
