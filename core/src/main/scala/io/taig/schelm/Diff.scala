package io.taig.schelm

import cats.data.NonEmptyList
import cats.implicits._

sealed abstract class Diff[+A] extends Product with Serializable

object Diff {
  final case class AddAttribute[A](attribute: Attribute[A]) extends Diff[A]
  final case class AddChild[A](key: Key, child: Html[A]) extends Diff[A]
  final case class Group[A](diffs: NonEmptyList[Diff[A]]) extends Diff[A]
  final case class RemoveAttribute(key: String) extends Diff[Nothing]
  final case class RemoveChild(key: Key) extends Diff[Nothing]
  final case class Replace[A](html: Html[A]) extends Diff[A]
  final case class UpdateAttribute[A](attribute: Attribute[A]) extends Diff[A]
  final case class UpdateChild[A](key: Key, diff: Diff[A]) extends Diff[A]
  final case class UpdateText(value: String) extends Diff[Nothing]

  def of[A](diffs: Diff[A]*): Option[Diff[A]] = from(diffs)

  def from[A](diffs: Iterable[Diff[A]]): Option[Diff[A]] = diffs match {
    case Nil          => None
    case head :: Nil  => head.some
    case head :: tail => Group(NonEmptyList(head, tail)).some
  }
}
