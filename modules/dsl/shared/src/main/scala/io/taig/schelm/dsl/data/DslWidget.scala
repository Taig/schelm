package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Listeners, Node, State, Widget}

import scala.annotation.tailrec

sealed abstract class DslWidget[+F[_], -Context] extends Product with Serializable {
  @tailrec
  final def toWidget: Widget[Context, State[F, CssNode[Node[F, Listeners[F], DslWidget[F, Context]]]]] = this match {
    case DslWidget.Pure(widget)                     => widget
    case component: DslWidget.Component[F, Context] => component.render.toWidget
  }
}

object DslWidget {
  final case class Pure[F[_], Context](
      widget: Widget[Context, State[F, CssNode[Node[F, Listeners[F], DslWidget[F, Context]]]]]
  ) extends DslWidget[F, Context]

  abstract class Component[+F[_], -Context] extends DslWidget[F, Context] {
    def render: DslWidget[F, Context]
  }
}
