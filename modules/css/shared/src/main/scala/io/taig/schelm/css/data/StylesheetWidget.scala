package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Node, Widget}

final case class StylesheetWidget[+Event, -Context](
    widget: Widget[Context, StylesheetNode[Event, Node[Event, StylesheetWidget[Event, Context]]]]
) extends AnyVal

object StylesheetWidget {
  def toStylesheetHtml[Event, Context](
      widget: StylesheetWidget[Event, Context],
      context: Context
  ): StylesheetHtml[Event] =
    widget.widget match {
      case widget: Widget.Patch[Context, StylesheetNode[Event, Node[Event, StylesheetWidget[Event, Context]]]] =>
        toStylesheetHtml(StylesheetWidget(widget.widget), widget.f(context))
      case Widget.Pure(node) => StylesheetHtml(node.map(_.map(toStylesheetHtml(_, context))))
      case Widget.Render(f)  => toStylesheetHtml(StylesheetWidget(f(context)), context)
    }
}
