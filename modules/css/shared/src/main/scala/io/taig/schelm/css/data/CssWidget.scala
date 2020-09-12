package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.data.{Component, Widget}

final case class CssWidget[+F[_], -Context](widget: Widget[Context, CssNode[Component[F, CssWidget[F, Context]]]])
    extends AnyVal

object CssWidget {
  def toStylesheetHtml[F[_], Context](css: CssWidget[F, Context], context: Context): CssHtml[F] =
    css.widget match {
      case patch: Widget.Patch[Context, CssNode[Component[F, CssWidget[F, Context]]]] =>
        toStylesheetHtml(CssWidget(patch.widget), patch.f(context))
      case Widget.Pure(node) => CssHtml(node.map(_.map(toStylesheetHtml(_, context))))
      case Widget.Render(f)  => toStylesheetHtml(CssWidget(f(context)), context)
    }
}
