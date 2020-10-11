package io.taig.schelm.dsl.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.css.data.WidgetStateCssHtml
import io.taig.schelm.dsl.data.{DslWidget, ReduxWidgetStateCssHtml}
import io.taig.schelm.util.FunctionKs

object DslWidgetRenderer {
  def pure[F[_], Event, Context]: DslWidget[F, Event, Context] => ReduxWidgetStateCssHtml[F, Event, Context] = {
    case widget: DslWidget.Pure[F, Event, Context] =>
      ReduxWidgetStateCssHtml(widget.redux.map(_.map(_.map(_.map(_.map(pure[F, Event, Context]))))))
    case widget: DslWidget.Component[F, Event, Context] => pure[F, Event, Context](widget.render)
  }

  def id[F[_], Event, Context]: Kleisli[Id, DslWidget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]] =
    Kleisli[Id, DslWidget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]](pure[F, Event, Context])

  def apply[F[_]: Applicative, Event, Context]
      : Renderer[F, DslWidget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]] =
    id[F, Event, Context].mapK(FunctionKs.liftId[F])
}
