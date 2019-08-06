package io.taig.schelm

sealed abstract class HtmlDiff[+A] extends Product with Serializable

object HtmlDiff {
  final case class Replace[A](html: Html[A]) extends HtmlDiff[A]
  final case class Select[A](key: Key, diff: HtmlDiff[A]) extends HtmlDiff[A]
  final case class UpdateText(value: String) extends HtmlDiff[Nothing]
}
