package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class HtmlReference[+Event](
    reference: NodeReference[Event, HtmlReference[Event]]
) extends AnyVal {
  def toNodes: List[Dom.Node] = reference match {
    case NodeReference.Element(_, dom) => List(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.toNodes)
    case NodeReference.Text(_, dom)    => List(dom)
  }
}
