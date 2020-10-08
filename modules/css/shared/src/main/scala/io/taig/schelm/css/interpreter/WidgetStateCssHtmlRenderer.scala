package io.taig.schelm.css.interpreter

import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.{StateCssHtml, WidgetStateCssHtml}
import io.taig.schelm.data.Widget

final class WidgetStateCssHtmlRenderer[F[_], Context]
    extends Renderer[Kleisli[F, Context, *], WidgetStateCssHtml[F, Context], StateCssHtml[F]] {
  override def render(html: WidgetStateCssHtml[F, Context]): Kleisli[F, Context, StateCssHtml[F]] = Kleisli { context =>
    html.widget match {
      case Widget.Patch(f, widget) => ???
      case Widget.Pure(node)       => ???
      case Widget.Render(f)        => ???
    }
  }
}
