package io.taig.schelm

import io.taig.schelm.algebra.Dom

package object data {
  type Html[F[_]] = Fix[Node[F, *]]

  object Html {
    @inline
    def apply[F[_]](node: Node[F, Html[F]]): Html[F] = Fix(node)
  }

  type HtmlReference[F[_]] = Fix[NodeReference[F, *]]

  object HtmlReference {
    @inline
    def apply[F[_]](reference: NodeReference[F, HtmlReference[F]]): HtmlReference[F] = Fix(reference)
  }

  implicit final class HtmlReferenceOps[F[_]](html: HtmlReference[F]) {
    def nodes: List[Dom.Node] = html.unfix match {
      case NodeReference.Element(_, dom) => List(dom)
      case NodeReference.Fragment(node)  => node.children.toList.flatMap(_.nodes)
      case NodeReference.Text(_, dom)    => List(dom)
    }
  }
}
