package io.taig.schelm.data

import cats.Functor

sealed abstract class Widget[-Context, +A] extends Product with Serializable {
  final def map[B](f: A => B): Widget[Context, B] = this match {
    case widget: Widget.Patch[Context, _, A] => Widget.Patch(widget.f, f(widget.widget))
    case Widget.Pure(value)                  => Widget.Pure(f(value))
    case Widget.Render(g)                    => Widget.Render(context => f(g(context)))
  }
}

object Widget {
  final case class Patch[Context, A, B](f: Context => A, widget: B) extends Widget[Context, B]

  final case class Pure[A](value: A) extends Widget[Any, A]

  final case class Render[Context, A](f: Context => A) extends Widget[Context, A]

  def run[Context, A](context: Context): Widget[Context, A] => A = {
    case widget: Patch[Context, _, A] => widget.widget
    case Pure(value)                  => value
    case Render(f)                    => f(context)
  }

  implicit def functor[Context]: Functor[Widget[Context, *]] = new Functor[Widget[Context, *]] {
    override def map[A, B](fa: Widget[Context, A])(f: A => B): Widget[Context, B] = fa.map(f)
  }
}
