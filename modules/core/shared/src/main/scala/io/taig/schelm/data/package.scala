package io.taig.schelm

package object data {
  type Html[F[_]] = Fix[Node[F, *]]

  object Html {
    @inline
    def apply[F[_]](node: Node[F, Html[F]]): Html[F] = Fix(node)
  }

  type NamespaceHtml[F[_]] = Fix[λ[A => Namespace[Node[F, A]]]]

  object NamespaceHtml {
    @inline
    def apply[F[_]](namespace: Namespace[Node[F, NamespaceHtml[F]]]): NamespaceHtml[F] =
      Fix[λ[A => Namespace[Node[F, A]]]](namespace)
  }

  type HtmlReference[F[_]] = Fix[NodeReference[F, *]]

  object HtmlReference {
    @inline
    def apply[F[_]](reference: NodeReference[F, HtmlReference[F]]): HtmlReference[F] = Fix(reference)
  }

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

  type StateTree = IdentifierTree[Map[Identifier, Any]]

  object StateTree {
    val Empty: IdentifierTree[Map[Identifier, Any]] = IdentifierTree(Map.empty, Map.empty)
  }
}
