package io.taig.schelm.dsl.data

import cats.implicits._
import io.taig.schelm.css.data.{CssHtml, CssNode}
import io.taig.schelm.data.{Node, Widget}

import scala.annotation.tailrec

sealed abstract class DslWidget[-Context] extends Product with Serializable

object DslWidget {
  final case class Pure[Context](widget: Widget[Context, CssNode[Node[DslWidget[Context]]]]) extends DslWidget[Context]

  abstract class Component[Context] extends DslWidget[Context] {
    def render: DslWidget[Context]
  }

  @tailrec
  def toWidget[Context](dsl: DslWidget[Context]): Widget[Context, CssNode[Node[DslWidget[Context]]]] =
    dsl match {
      case Pure(widget)                  => widget
      case component: Component[Context] => toWidget(component.render)
    }

  def toCssHtml[Context](widget: Widget[Context, CssNode[Node[DslWidget[Context]]]], context: Context): CssHtml =
    widget match {
      case widget: Widget.Patch[Context, CssNode[Node[DslWidget[Context]]]] =>
        toCssHtml(widget.widget, widget.f(context))
      case Widget.Pure(component) => CssHtml(component.map(_.map(dsl => toCssHtml(toWidget(dsl), context))))
      case Widget.Render(f)       => toCssHtml(f(context), context)
    }
}
