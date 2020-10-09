package io.taig.schelm.dsl.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.WidgetStateCssHtml
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.util.FunctionKs

object DslWidgetRenderer {
  def pure[F[_], Context]: DslWidget[F, Context] => WidgetStateCssHtml[F, Context] = {
    case widget: DslWidget.Pure[F, Context] =>
      WidgetStateCssHtml(widget.widget.map(_.map(_.map(_.map(pure[F, Context])))))
    case widget: DslWidget.Component[F, Context] => pure[F, Context](widget.render)
  }

  def id[F[_], Context]: Kleisli[Id, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] =
    Kleisli[Id, DslWidget[F, Context], WidgetStateCssHtml[F, Context]](pure[F, Context])

  def apply[F[_]: Applicative, Context]: Renderer[F, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] =
    id[F, Context].mapK(FunctionKs.liftId[F])
}
