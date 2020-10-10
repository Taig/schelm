package io.taig.schelm.dsl.data

import io.taig.schelm.css.data.Css
import io.taig.schelm.data.{Listeners, Node, State, Widget}

import scala.annotation.tailrec

sealed abstract class DslWidget[+F[_], -Context] extends Product with Serializable {
  @tailrec
  final def apply(context: Context): State[F, Css[Node[F, Listeners[F], DslWidget[F, Context]]]] = this match {
    case DslWidget.Pure(Widget.Patch(_, widget)) => widget
    case DslWidget.Pure(Widget.Pure(value)) => value
    case DslWidget.Pure(Widget.Render(f)) => f(context)
    case widget: DslWidget.Component[F, Context] => widget.render.apply(context)
  }
}

object DslWidget {
  final case class Pure[F[_], Context](
  widget: Widget[Context, State[F, Css[Node[F, Listeners[F], DslWidget[F, Context]]]]]
  ) extends DslWidget[F, Context]

  abstract class Component[+F[_], -Context] extends DslWidget[F, Context] {
    def render: DslWidget[F, Context]
  }
}
