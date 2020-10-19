package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css.data.{Css, Style}
import io.taig.schelm.data._
import io.taig.schelm.redux.data.Redux

sealed abstract class Widget[+F[_], +Event, -Context] {
  final def modifyAttributes(f: Attributes => Attributes): Widget[F, Event, Context] =
    this match {
      case widget: Widget.Pure[F, Event, Context] =>
        Widget.Pure(widget.redux.map(_.map(_.map(_.map(_.modifyAttributes(f))))))
      case widget: Component[F, Event, Context] => widget.render.modifyAttributes(f)
    }

  final def modifyListeners[G[A] >: F[A]](f: Listeners[G] => Listeners[G]): Widget[G, Event, Context] =
    this match {
      case widget: Widget.Pure[G, Event, Context] =>
        Widget.Pure(widget.redux.map(_.map(_.map(_.map(_.modifyListeners(f))))))
      case widget: Component[G, Event, Context] => widget.render.modifyListeners(f)
    }

  final def modifyChildren[G[A] >: F[A], E >: Event, C <: Context](
      f: Children[Widget[G, E, C]] => Children[Widget[G, E, C]]
  ): Widget[G, E, C] =
    this match {
      case widget: Widget.Pure[G, Event, Context] =>
        Widget.Pure(widget.redux.map(_.map(_.map(_.map(_.modifyChildren(f))))))
      case widget: Component[G, Event, Context] => widget.render.modifyChildren(f)
    }

  final def modifyStyle(f: Style => Style): Widget[F, Event, Context] =
    this match {
      case widget: Widget.Pure[F, Event, Context] =>
        Widget.Pure(widget.redux.map(_.map(_.map(_.modifyStyle(f)))))
      case widget: Component[F, Event, Context] => widget.render.modifyStyle(f)
    }
}

object Widget {
  final case class Pure[F[_], Event, Context](
      redux: Redux[F, Event, Contextual[Context, State[F, Css[Node[F, Listeners[F], Widget[F, Event, Context]]]]]]
  ) extends Widget[F, Event, Context]

  final def run[F[_], Event, Context](
      widget: Widget[F, Event, Context]
  ): Redux[F, Event, Contextual[Context, State[F, Css[Node[F, Listeners[F], Widget[F, Event, Context]]]]]] =
    widget match {
      case widget: Pure[F, Event, Context]      => widget.redux
      case widget: Component[F, Event, Context] => run(widget.render)
    }
}

abstract class Component[+F[_], +Event, -Context] extends Widget[F, Event, Context] {
  def render: Widget[F, Event, Context]
}
