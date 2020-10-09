package io.taig.schelm

package object data {
  type Html[F[_]] = Fix[Node[F, Listeners[F], *]]

  object Html {
    def apply[F[_]](node: Node[F, Listeners[F], Html[F]]): Html[F] = Fix(node)
  }

  type StateHtml[F[_]] = Fix[λ[A => State[F, Node[F, Listeners[F], A]]]]

  type WidgetHtml[F[_], Context] = Fix[λ[A => Widget[Context, Node[F, Listeners[F], A]]]]

  type WidgetStateHtml[F[_], Context] = Fix[λ[A => Widget[Context, State[F, Node[F, Listeners[F], A]]]]]
}
