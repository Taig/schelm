package io.taig.schelm

package object data {
  type Html[F[_]] = Fix[Node[F, Listeners[F], *]]

  object Html {
    def apply[F[_]](node: Node[F, Listeners[F], Html[F]]): Html[F] = Fix(node)
  }

  type StateHtml[F[_]] = Fix[λ[A => State[F, Node[F, Listeners[F], A]]]]

  object StateHtml {
    def apply[F[_]](state: State[F, Node[F, Listeners[F], StateHtml[F]]]): StateHtml[F] =
      Fix[λ[A => State[F, Node[F, Listeners[F], A]]]](state)
  }

  type WidgetHtml[F[_], Context] = Fix[λ[A => Contextual[Context, Node[F, Listeners[F], A]]]]

  object WidgetHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, Node[F, Listeners[F], WidgetHtml[F, Context]]]
    ): WidgetHtml[F, Context] =
      Fix[λ[A => Contextual[Context, Node[F, Listeners[F], A]]]](widget)
  }

  type WidgetStateHtml[F[_], Context] = Fix[λ[A => Contextual[Context, State[F, Node[F, Listeners[F], A]]]]]

  object WidgetStateHtml {
    def apply[F[_], Context](
        widget: Contextual[Context, State[F, Node[F, Listeners[F], WidgetStateHtml[F, Context]]]]
    ): WidgetStateHtml[F, Context] =
      Fix[λ[A => Contextual[Context, State[F, Node[F, Listeners[F], A]]]]](widget)
  }
}
