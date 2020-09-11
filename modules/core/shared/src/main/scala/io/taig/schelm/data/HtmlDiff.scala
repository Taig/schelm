package io.taig.schelm.data

import cats.data.NonEmptyList

sealed abstract class HtmlDiff[+F[_]] extends Product with Serializable

object HtmlDiff {
  final case class AddAttribute(attribute: Attribute) extends HtmlDiff[Nothing]
  final case class AppendChild[F[_]](html: Html[F]) extends HtmlDiff[F]
  final case class AddListener(listener: Listener) extends HtmlDiff[Nothing]
  final case object Clear extends HtmlDiff[Nothing]
  final case class Group[F[_]](diffs: NonEmptyList[HtmlDiff[F]]) extends HtmlDiff[F]
  final case class Replace[F[_]](html: Html[F]) extends HtmlDiff[F]
  final case class RemoveAttribute(key: Attribute.Key) extends HtmlDiff[Nothing]
  final case class RemoveChild(index: Int) extends HtmlDiff[Nothing]
  final case class RemoveListener(name: Listener.Name) extends HtmlDiff[Nothing]
  final case class UpdateAttribute(key: Attribute.Key, value: Attribute.Value) extends HtmlDiff[Nothing]
  final case class UpdateChild[F[_]](index: Int, diff: HtmlDiff[F]) extends HtmlDiff[F]
  final case class UpdateListener(name: Listener.Name, action: Listener.Action) extends HtmlDiff[Nothing]
  final case class UpdateText(value: String) extends HtmlDiff[Nothing]

  def from[F[_]](diffs: List[HtmlDiff[F]]): Option[HtmlDiff[F]] = diffs match {
    case Nil          => None
    case head :: Nil  => Some(head)
    case head :: tail => Some(Group(NonEmptyList(head, tail)))
  }
}
