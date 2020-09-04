package io.taig.schelm.data

import cats.Functor

sealed abstract class Widget[-Context, +A] extends Product with Serializable

object Widget {
  final case class Patch[Context, +A](f: Context => Context, widget: Widget[Context, A]) extends Widget[Context, A]

  final case class Pure[+A](node: A) extends Widget[Any, A]

  final case class Render[-Context, +A](f: Context => Widget[Context, A]) extends Widget[Context, A]

  implicit def functor[Context]: Functor[Widget[Context, *]] = new Functor[Widget[Context, *]] {
    override def map[A, B](fa: Widget[Context, A])(f: A => B): Widget[Context, B] =
      fa match {
        case widget: Patch[Context, A] => Patch(widget.f, map(widget.widget)(f))
        case Pure(node)                => Pure(f(node))
        case Render(g)                 => Render(context => map(g(context))(f))
      }
  }
}
