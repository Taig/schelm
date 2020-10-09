package io.taig.schelm.css

import io.taig.schelm.data._

package object data {
  type CssHtml[F[_]] = Fix[λ[A => CssNode[Node[F, Listeners[F], A]]]]

  object CssHtml {
    def apply[F[_]](css: CssNode[Node[F, Listeners[F], CssHtml[F]]]): CssHtml[F] =
      Fix[λ[A => CssNode[Node[F, Listeners[F], A]]]](css)
  }

  type StateCssHtml[F[_]] = Fix[λ[A => State[F, CssNode[Node[F, Listeners[F], A]]]]]

  object StateCssHtml {
    def apply[F[_]](state: State[F, CssNode[Node[F, Listeners[F], StateCssHtml[F]]]]): StateCssHtml[F] =
      Fix[λ[A => State[F, CssNode[Node[F, Listeners[F], A]]]]](state)
  }

  type WidgetCssHtml[F[_], Context] = Fix[λ[A => Widget[Context, CssNode[Node[F, Listeners[F], A]]]]]

  object WidgetCssHtml {
    def apply[F[_], Context](
        widget: Widget[Context, CssNode[Node[F, Listeners[F], WidgetCssHtml[F, Context]]]]
    ): WidgetCssHtml[F, Context] =
      Fix[λ[A => Widget[Context, CssNode[Node[F, Listeners[F], A]]]]](widget)
  }

  type WidgetStateCssHtml[F[_], Context] = Fix[λ[A => Widget[Context, State[F, CssNode[Node[F, Listeners[F], A]]]]]]

  object WidgetStateCssHtml {
    def apply[F[_], Context](
        widget: Widget[Context, State[F, CssNode[Node[F, Listeners[F], WidgetStateCssHtml[F, Context]]]]]
    ): WidgetStateCssHtml[F, Context] =
      Fix[λ[A => Widget[Context, State[F, CssNode[Node[F, Listeners[F], A]]]]]](widget)
  }
}
