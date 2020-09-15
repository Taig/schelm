package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Node, Widget}

final case class CssWidget[-Context](widget: Widget[Context, CssNode[Node[CssWidget[Context]]]]) extends AnyVal

object CssWidget {
  def toStylesheetHtml[Context](css: CssWidget[Context], context: Context): CssHtml =
    css.widget match {
      case patch: Widget.Patch[Context, CssNode[Node[CssWidget[Context]]]] =>
        toStylesheetHtml(CssWidget(patch.widget), patch.f(context))
      case Widget.Pure(component) => CssHtml(component.map(_.map(toStylesheetHtml(_, context))))
      case Widget.Render(f)       => toStylesheetHtml(CssWidget(f(context)), context)
    }
}
