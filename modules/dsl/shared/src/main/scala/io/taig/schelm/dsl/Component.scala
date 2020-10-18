package io.taig.schelm.dsl

import io.taig.schelm.css.data.Css
import io.taig.schelm.data.{Contextual, Listeners, Node, State}
import io.taig.schelm.redux.data.Redux

sealed abstract class Widget[+F[_], +Event, -Context]

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
