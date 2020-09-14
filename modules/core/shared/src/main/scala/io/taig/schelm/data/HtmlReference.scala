package io.taig.schelm.data

final case class HtmlReference[Node, Element <: Node, Text <: Node](
    reference: ComponentReference[Element, Text, HtmlReference[Node, Element, Text]]
) extends AnyVal {
  def toNodes: List[Node] = reference match {
    case ComponentReference.Element(_, dom)     => List(dom)
    case ComponentReference.Fragment(component) => component.children.indexed.flatMap(_.toNodes)
    case ComponentReference.Text(_, dom)        => List(dom)
  }
}
