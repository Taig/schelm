package io.taig.schelm.dsl.data

import cats.implicits._
import io.taig.schelm.css.data.{CssHtml, CssNode}
import io.taig.schelm.data.{Node, Widget}

import scala.annotation.tailrec

sealed abstract class DslWidget[+Event, -Context]

object DslWidget {
  final case class Pure[Event, Context](widget: Widget[Context, CssNode[Node[Event, DslWidget[Event, Context]]]])
      extends DslWidget[Event, Context]

  abstract class Component[+Event, -Context] extends DslWidget[Event, Context] {
    def render: DslWidget[Event, Context]
  }

  @tailrec
  def toWidget[Event, Context](
      dsl: DslWidget[Event, Context]
  ): Widget[Context, CssNode[Node[Event, DslWidget[Event, Context]]]] =
    dsl match {
      case Pure(widget)                         => widget
      case component: Component[Event, Context] => toWidget(component.render)
    }

  def toCssHtml[Event, Context](
      widget: Widget[Context, CssNode[Node[Event, DslWidget[Event, Context]]]],
      context: Context
  ): CssHtml[Event] =
    widget match {
      case widget: Widget.Patch[Context, CssNode[Node[Event, DslWidget[Event, Context]]]] =>
        toCssHtml(widget.widget, widget.f(context))
      case Widget.Pure(component) => CssHtml(component.map(_.map(dsl => toCssHtml(toWidget(dsl), context))))
      case Widget.Render(f)       => toCssHtml(f(context), context)
    }
}
