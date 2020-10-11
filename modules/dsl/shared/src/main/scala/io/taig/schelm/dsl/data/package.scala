package io.taig.schelm.dsl

import io.taig.schelm.css.data.Css
import io.taig.schelm.data._
import io.taig.schelm.redux.data.Redux

package object data {
  type ReduxWidgetStateCssHtml[F[_], Event, Context] =
    Fix[λ[A => Redux[F, Event, Widget[Context, State[F, Css[Node[F, Listeners[F], A]]]]]]]

  object ReduxWidgetStateCssHtml {
    def apply[F[_], Event, Context](
        redux: Redux[
          F,
          Event,
          Widget[Context, State[F, Css[Node[F, Listeners[F], ReduxWidgetStateCssHtml[F, Event, Context]]]]]
        ]
    ): ReduxWidgetStateCssHtml[F, Event, Context] =
      Fix[λ[A => Redux[F, Event, Widget[Context, State[F, Css[Node[F, Listeners[F], A]]]]]]](redux)
  }
}
