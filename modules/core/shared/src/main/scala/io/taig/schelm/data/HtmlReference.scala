package io.taig.schelm.data

final case class HtmlReference[F[_], Node, Element <: Node, Text <: Node](
    reference: ComponentReference[F, Element, Text, HtmlReference[F, Node, Element, Text]]
) {
  def toNodes: List[Node] = reference match {
    case ComponentReference.Element(_, dom)     => List(dom)
    case ComponentReference.Fragment(component) => component.children.indexed.flatMap(_.toNodes)
    case ComponentReference.Text(_, dom)        => List(dom)
  }
}
