package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Node, Widget}

final case class CssWidget[+Event, -Context](widget: Widget[Context, CssNode[Node[Event, CssWidget[Event, Context]]]])
    extends AnyVal

object CssWidget {
  def toStylesheetHtml[Event, Context](css: CssWidget[Event, Context], context: Context): CssHtml[Event] =
    css.widget match {
      case patch: Widget.Patch[Context, CssNode[Node[Event, CssWidget[Event, Context]]]] =>
        toStylesheetHtml(CssWidget(patch.widget), patch.f(context))
      case Widget.Pure(node) => CssHtml(node.map(_.map(toStylesheetHtml(_, context))))
      case Widget.Render(f)  => toStylesheetHtml(CssWidget(f(context)), context)
    }
}
