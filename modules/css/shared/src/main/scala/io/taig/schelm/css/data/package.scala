package io.taig.schelm.css

import io.taig.schelm.data._

package object data {
  type CssHtml[F[_]] = Fix[λ[A => Css[Node[F, Listeners[F], A]]]]

  object CssHtml {
    def apply[F[_]](css: Css[Node[F, Listeners[F], CssHtml[F]]]): CssHtml[F] =
      Fix[λ[A => Css[Node[F, Listeners[F], A]]]](css)
  }

  type StateCssHtml[F[_]] = Fix[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]]

  object StateCssHtml {
    def apply[F[_]](state: State[F, Css[Node[F, Listeners[F], StateCssHtml[F]]]]): StateCssHtml[F] =
      Fix[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]](state)
  }

  type WidgetCssHtml[F[_], Context] = Fix[λ[A => Contextual[Context, Css[Node[F, Listeners[F], A]]]]]

  object WidgetCssHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, Css[Node[F, Listeners[F], WidgetCssHtml[F, Context]]]]
    ): WidgetCssHtml[F, Context] =
      Fix[λ[A => Contextual[Context, Css[Node[F, Listeners[F], A]]]]](widget)
  }

  type WidgetStateCssHtml[F[_], Context] = Fix[λ[A => Contextual[Context, State[F, Css[Node[F, Listeners[F], A]]]]]]

  object WidgetStateCssHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, State[F, Css[Node[F, Listeners[F], WidgetStateCssHtml[F, Context]]]]]
    ): WidgetStateCssHtml[F, Context] =
      Fix[λ[A => Contextual[Context, State[F, Css[Node[F, Listeners[F], A]]]]]](widget)
  }
}
