package io.taig.schelm.data

import cats.data.NonEmptyList

sealed abstract class HtmlDiff extends Product with Serializable

object HtmlDiff {
  final case class AddAttribute(attribute: Attribute) extends HtmlDiff
  final case class AppendChild(html: Html[Nothing]) extends HtmlDiff
  final case class AddListener(listener: Listener) extends HtmlDiff
  final case object Clear extends HtmlDiff
  final case class Group(diffs: NonEmptyList[HtmlDiff]) extends HtmlDiff
  final case class Replace(html: Html[Nothing]) extends HtmlDiff
  final case class RemoveAttribute(key: Attribute.Key) extends HtmlDiff
  final case class RemoveChild(index: Int) extends HtmlDiff
  final case class RemoveListener(name: Listener.Name) extends HtmlDiff
  final case class UpdateAttribute(key: Attribute.Key, value: Attribute.Value) extends HtmlDiff
  final case class UpdateChild(index: Int, diff: HtmlDiff) extends HtmlDiff
  final case class UpdateListener(name: Listener.Name, action: Listener.Action) extends HtmlDiff
  final case class UpdateText(value: String) extends HtmlDiff

  def from(diffs: List[HtmlDiff]): Option[HtmlDiff] = diffs match {
    case Nil          => None
    case head :: Nil  => Some(head)
    case head :: tail => Some(Group(NonEmptyList(head, tail)))
  }
}
