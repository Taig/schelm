package io.taig.schelm

import cats.data.NonEmptyList
import cats.implicits._

sealed abstract class HtmlDiff[+A] extends Product with Serializable

object HtmlDiff {
  // format: off
  final case class AddAttribute(attribute: Attribute)                   extends HtmlDiff[Nothing]
  final case class AddChild[A](key: Key, html: Html[A])                 extends HtmlDiff[A]
  final case class AddListener[A](listener: Listener[A])                extends HtmlDiff[A]
  final case object Clear                                               extends HtmlDiff[Nothing]
  final case class Group[A](diffs: NonEmptyList[HtmlDiff[A]])           extends HtmlDiff[A]
  final case class Replace[A](html: Html[A])                            extends HtmlDiff[A]
  final case class RemoveAttribute(key: String)                         extends HtmlDiff[Nothing]
  final case class RemoveChild(key: Key)                                extends HtmlDiff[Nothing]
  final case class RemoveListener(event: String)                        extends HtmlDiff[Nothing]
  final case class UpdateAttribute(key: String, value: Value)           extends HtmlDiff[Nothing]
  final case class UpdateChild[A](key: Key, diff: HtmlDiff[A])          extends HtmlDiff[A]
  final case class UpdateListener[A](event: String, action: Action[A])  extends HtmlDiff[A]
  final case class UpdateText(value: String)                            extends HtmlDiff[Nothing]
  // format: on

  def addChild[A](t: (Key, Html[A])): HtmlDiff[A] = AddChild(t._1, t._2)

  def of[A](diffs: HtmlDiff[A]*): Option[HtmlDiff[A]] = from(diffs)

  def from[A](diffs: Iterable[HtmlDiff[A]]): Option[HtmlDiff[A]] = diffs match {
    case Nil          => None
    case head :: Nil  => head.some
    case head :: tail => Group(NonEmptyList(head, tail)).some
  }

}
