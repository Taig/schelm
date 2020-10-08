package io.taig.schelm.css

import io.taig.schelm.data.{Listeners, Node, State, Widget}

package object data {
  type CssHtml[F[_]] = CssNode.⟳[Node[F, Listeners[F], *]]

  type StateCssHtml[F[_]] = State.⟳[F, λ[A => CssNode[Node[F, Listeners[F], A]]]]

  type WidgetCssHtml[F[_], Context] = Widget.⟳[Context, λ[A => CssNode[Node[F, Listeners[F], A]]]]

  type WidgetStateCssHtml[F[_], Context] = Widget.⟳[Context, λ[A => State[F, CssNode[Node[F, Listeners[F], A]]]]]
}
