package io.taig.schelm.data

import cats.Functor

sealed abstract class Widget[-Context, +A] extends Product with Serializable {
  final def map[B](f: A => B): Widget[Context, B] = this match {
    case widget: Widget.Patch[Context, A] => Widget.Patch(widget.f, widget.widget.map(f))
    case Widget.Pure(value)               => Widget.Pure(f(value))
    case Widget.Render(g)                 => Widget.Render(context => g(context).map(f))
  }
}

object Widget {
  type ⟳[Context, F[_]] = Widget[Context, Fix[λ[A => F[Widget[Context, A]]]]]

  final case class Patch[Context, +A](f: Context => Context, widget: Widget[Context, A]) extends Widget[Context, A]

  final case class Pure[+A](value: A) extends Widget[Any, A]

  final case class Render[-Context, +A](f: Context => Widget[Context, A]) extends Widget[Context, A]

  implicit def functor[Context]: Functor[Widget[Context, *]] = new Functor[Widget[Context, *]] {
    override def map[A, B](fa: Widget[Context, A])(f: A => B): Widget[Context, B] = fa.map(f)
  }
}
