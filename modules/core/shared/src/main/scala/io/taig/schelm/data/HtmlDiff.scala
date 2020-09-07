package io.taig.schelm.data

import cats.data.NonEmptyList

sealed abstract class HtmlDiff[+Event] extends Product with Serializable

object HtmlDiff {
  final case class AddAttribute(attribute: Attribute) extends HtmlDiff[Nothing]
  final case class AppendChild[Event](html: Html[Event]) extends HtmlDiff[Event]
  final case class AddListener[Event](listener: Listener[Event]) extends HtmlDiff[Event]
  final case object Clear extends HtmlDiff[Nothing]
  final case class Group[A](diffs: NonEmptyList[HtmlDiff[A]]) extends HtmlDiff[A]
  final case class Replace[Event](html: Html[Event]) extends HtmlDiff[Event]
  final case class RemoveAttribute(key: Attribute.Key) extends HtmlDiff[Nothing]
  final case class RemoveChild(index: Int) extends HtmlDiff[Nothing]
  final case class RemoveListener(name: Listener.Name) extends HtmlDiff[Nothing]
  final case class UpdateAttribute(key: Attribute.Key, value: Attribute.Value) extends HtmlDiff[Nothing]
  final case class UpdateChild[A](index: Int, diff: HtmlDiff[A]) extends HtmlDiff[A]
  final case class UpdateListener[A](name: Listener.Name, action: Listener.Action[A]) extends HtmlDiff[A]
  final case class UpdateText(value: String) extends HtmlDiff[Nothing]

  def from[Event](diffs: List[HtmlDiff[Event]]): Option[HtmlDiff[Event]] = diffs match {
    case Nil          => None
    case head :: Nil  => Some(head)
    case head :: tail => Some(Group(NonEmptyList(head, tail)))
  }
}
