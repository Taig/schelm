//package io.taig.schelm.data
//
//import cats.Id
//
//final case class HtmlReference[+Node, +Element <: Node, +Text <: Node](
//    reference: NodeReference[Pure, Element, Text]
//) extends AnyVal {
//  def toNodes: List[Node] = reference match {
//    case NodeReference.Element(_, dom) => List(dom)
//    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.toNodes)
//    case NodeReference.Text(_, dom)    => List(dom)
//  }
//}
//
//object HtmlReference {
//  val x: NodeReference[Pure, Nothing, Nothing] = ???
//  x match {
//    case NodeReference.Element(component, dom) =>
//    case NodeReference.Fragment(component) => component.children.indexed.map
//    case NodeReference.Text(component, dom) =>
//  }
//}