package io.taig.schelm.data

final case class HtmlReference[+Event, Node, Element <: Node, Text <: Node](
    reference: NodeReference[Event, Element, Text, HtmlReference[Event, Node, Element, Text]]
) extends AnyVal {
  def toNodes: List[Node] = reference match {
    case NodeReference.Element(_, dom) => List(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.toNodes)
    case NodeReference.Text(_, dom)    => List(dom)
  }
}
