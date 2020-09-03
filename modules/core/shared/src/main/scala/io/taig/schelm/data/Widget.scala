package io.taig.schelm.data

sealed abstract class Widget[+F[+_], +Context] extends Product with Serializable

object Widget {
  final case class Patch[F[+_], Context](f: Context => Context, widget: Widget[F, Context]) extends Widget[F, Context]

  final case class Pure[F[+_], Context](node: F[Context]) extends Widget[F, Context]

  final case class Render[F[+_], Context](f: Context => Widget[F, Context]) extends Widget[F, Context]
}
