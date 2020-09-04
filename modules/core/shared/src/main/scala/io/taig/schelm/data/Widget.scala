package io.taig.schelm.data

sealed abstract class Widget[+A, -Context] extends Product with Serializable

object Widget {
  final case class Patch[+A, -Context, B](f: Context => B, widget: Widget[A, B]) extends Widget[A, B]

  final case class Pure[+A](node: A) extends Widget[A, Any]

  final case class Render[+A, -Context](f: Context => Widget[A, Context]) extends Widget[A, Context]
}
