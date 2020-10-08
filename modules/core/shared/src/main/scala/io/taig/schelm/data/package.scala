package io.taig.schelm

package object data {
  type Html[F[_]] = Node[F, Listeners[F], Fix[Node[F, Listeners[F], *]]]

  type StateHtml[F[_]] = State.⟳[F, Node[F, Listeners[F], *]]

  type WidgetHtml[F[_], Context] = Widget.⟳[Context, Node[F, Listeners[F], *]]

  type WidgetStateHtml[F[_], Context] = Widget.⟳[Context, λ[A => State[F, Node[F, Listeners[F], A]]]]
}
