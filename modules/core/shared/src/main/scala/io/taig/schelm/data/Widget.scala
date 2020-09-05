package io.taig.schelm.data

import cats.Functor
import io.taig.schelm.Navigator

sealed abstract class Widget[-Context, +A] extends Product with Serializable {
  final def map[B](f: A => B): Widget[Context, B] = this match {
    case widget: Widget.Patch[Context, A] => Widget.Patch(widget.f, widget.widget.map(f))
    case Widget.Pure(node)                => Widget.Pure(f(node))
    case Widget.Render(g)                 => Widget.Render(context => g(context).map(f))
  }
}

object Widget {
  final case class Patch[Context, +A](f: Context => Context, widget: Widget[Context, A]) extends Widget[Context, A]

  final case class Pure[+A](node: A) extends Widget[Any, A]

  final case class Render[-Context, +A](f: Context => Widget[Context, A]) extends Widget[Context, A]

  implicit def functor[Context]: Functor[Widget[Context, *]] = new Functor[Widget[Context, *]] {
    override def map[A, B](fa: Widget[Context, A])(f: A => B): Widget[Context, B] = fa.map(f)
  }

  implicit def navigator[Event, Context, F[_], A](
      implicit navigator: Navigator[Event, F[A], A]
  ): Navigator[Event, Widget[Context, F[A]], A] = new Navigator[Event, Widget[Context, F[A]], A] {
    override def attributes(widget: Widget[Context, F[A]], f: Attributes => Attributes): Widget[Context, F[A]] =
      widget match {
        case widget: Patch[Context, F[A]]  => Patch(widget.f, attributes(widget.widget, f))
        case widget: Pure[F[A]]            => Pure(navigator.attributes(widget.node, f))
        case widget: Render[Context, F[A]] => Render(context => attributes(widget.f(context), f))
      }

    override def listeners(
        widget: Widget[Context, F[A]],
        f: Listeners[Event] => Listeners[Event]
    ): Widget[Context, F[A]] =
      widget match {
        case widget: Patch[Context, F[A]]  => Patch(widget.f, listeners(widget.widget, f))
        case widget: Pure[F[A]]            => Pure(navigator.listeners(widget.node, f))
        case widget: Render[Context, F[A]] => Render(context => listeners(widget.f(context), f))
      }

    override def children(widget: Widget[Context, F[A]], f: Children[A] => Children[A]): Widget[Context, F[A]] =
      widget match {
        case widget: Patch[Context, F[A]]  => Patch(widget.f, children(widget.widget, f))
        case widget: Pure[F[A]]            => Pure(navigator.children(widget.node, f))
        case widget: Render[Context, F[A]] => Render(context => children(widget.f(context), f))
      }
  }
}
