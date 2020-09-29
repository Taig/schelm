package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class HtmlReference[F[_]](reference: NodeReference[F, HtmlReference[F]]) extends AnyVal {
  def toNodes: List[Dom.Node] = reference match {
    case NodeReference.Element(_, dom) => List(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.toNodes)
    case NodeReference.Text(_, dom)    => List(dom)
  }
}
