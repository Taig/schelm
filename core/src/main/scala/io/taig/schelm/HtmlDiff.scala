package io.taig.schelm

import cats.data.NonEmptyList
import cats.implicits._

sealed abstract class HtmlDiff[+A] extends Product with Serializable

object HtmlDiff {
  final case class AddAttribute[A](attribute: Attribute[A]) extends HtmlDiff[A]
  final case class AddChild[A](key: Key, child: Html[A]) extends HtmlDiff[A]
  final case class Group[A](diffs: NonEmptyList[HtmlDiff[A]])
      extends HtmlDiff[A]
  final case class RemoveAttribute(key: String) extends HtmlDiff[Nothing]
  final case class RemoveChild(key: Key) extends HtmlDiff[Nothing]
  final case class Replace[A](html: Html[A]) extends HtmlDiff[A]
  final case class UpdateAttribute[A](attribute: Attribute[A])
      extends HtmlDiff[A]
  final case class UpdateChild[A](key: Key, diff: HtmlDiff[A])
      extends HtmlDiff[A]
  final case class UpdateText(value: String) extends HtmlDiff[Nothing]

  def of[A](diffs: HtmlDiff[A]*): Option[HtmlDiff[A]] = from(diffs)

  def from[A](diffs: Iterable[HtmlDiff[A]]): Option[HtmlDiff[A]] = diffs match {
    case Nil          => None
    case head :: Nil  => head.some
    case head :: tail => Group(NonEmptyList(head, tail)).some
  }
}
