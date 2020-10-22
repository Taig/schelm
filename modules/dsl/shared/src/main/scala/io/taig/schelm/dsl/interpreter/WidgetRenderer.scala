package io.taig.schelm.dsl.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.dsl.Widget
import io.taig.schelm.dsl.data.ReduxWidgetStateCssHtml
import io.taig.schelm.util.FunctionKs

object WidgetRenderer {
  def pure[F[_], Event, Context]: Widget[F, Event, Context] => ReduxWidgetStateCssHtml[F, Event, Context] =
    widget => ReduxWidgetStateCssHtml(widget.redux.map(_.map(_.map(_.map(_.map(pure[F, Event, Context]))))))

  def id[F[_], Event, Context]: Renderer[Id, Widget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]] =
    Kleisli[Id, Widget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]](pure[F, Event, Context])

  def apply[F[_]: Applicative, Event, Context]
      : Renderer[F, Widget[F, Event, Context], ReduxWidgetStateCssHtml[F, Event, Context]] =
    id[F, Event, Context].mapK(FunctionKs.liftId[F])
}
