package io.taig.schelm.data

import cats.Functor

sealed abstract class Contextual[-Context, +A] extends Product with Serializable {
  final def map[B](f: A => B): Contextual[Context, B] = this match {
    case widget: Contextual.Patch[Context, _, A] => Contextual.Patch(widget.f, f(widget.widget))
    case Contextual.Pure(value)                  => Contextual.Pure(f(value))
    case Contextual.Render(g)                    => Contextual.Render(context => f(g(context)))
  }
}

object Contextual {
  final case class Patch[Context, A, B](f: Context => A, widget: B) extends Contextual[Context, B]

  final case class Pure[A](value: A) extends Contextual[Any, A]

  final case class Render[Context, A](f: Context => A) extends Contextual[Context, A]

  def run[Context, A](context: Context): Contextual[Context, A] => A = {
    case widget: Patch[Context, _, A] => widget.widget
    case Pure(value)                  => value
    case Render(f)                    => f(context)
  }

  implicit def functor[Context]: Functor[Contextual[Context, *]] = new Functor[Contextual[Context, *]] {
    override def map[A, B](fa: Contextual[Context, A])(f: A => B): Contextual[Context, B] = fa.map(f)
  }
}
