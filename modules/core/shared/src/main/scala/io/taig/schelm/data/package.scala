package io.taig.schelm

package object data {
  type WidgetHtml[F[_], Context] = Fix[λ[A => Contextual[Context, Node[F, A]]]]

  object WidgetHtml {
    @inline
    def apply[F[_], Context](
        widget: Contextual[Context, Node[F, WidgetHtml[F, Context]]]
    ): WidgetHtml[F, Context] =
      Fix[λ[A => Contextual[Context, Node[F, A]]]](widget)
  }

  type WidgetStateHtml[F[_], Context] = Fix[λ[A => Contextual[Context, State[F, Node[F, A]]]]]

  object WidgetStateHtml {
    @inline
    def apply[F[_], Context](
        widget: Contextual[Context, State[F, Node[F, WidgetStateHtml[F, Context]]]]
    ): WidgetStateHtml[F, Context] =
      Fix[λ[A => Contextual[Context, State[F, Node[F, A]]]]](widget)
  }
}
