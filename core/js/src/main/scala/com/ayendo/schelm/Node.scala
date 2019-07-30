package com.ayendo.schelm

import org.scalajs.dom

sealed abstract class Node[+A] extends Product with Serializable {
  final def root: List[dom.Node] = this match {
    case Node.Element(_, node)    => List(node)
    case Node.Fragment(component) => component.children.values.flatMap(_.root)
    case Node.Text(_, node)       => List(node)
  }
}

object Node {
  final case class Element[A](
      component: Component.Element[Node[A], A],
      node: dom.Element
  ) extends Node[A]

  final case class Fragment[A](
      component: Component.Fragment[Node[A], A]
  ) extends Node[A]

  final case class Text(component: Component.Text, node: dom.Text)
      extends Node[Nothing]

  private def extract[A](node: Node[A]): Component[Node[A], A] = node match {
    case Element(component, _) => component
    case Fragment(component)   => component
    case Text(component, _)    => component
  }

  private def inject[A](
      component: Component[Node[A], A],
      node: Node[A]
  ): Node[A] =
    (component, node) match {
      case (component: Component.Fragment[Node[A], A], _: Node.Fragment[A]) =>
        Fragment(component)
      case (component: Component.Element[Node[A], A], Element(_, node)) =>
        Element(component, node)
      case (component: Component.Text, Text(_, node)) => Text(component, node)
      case _                                          => node
    }

  implicit final class NodeOps[A](node: Node[A])
      extends ComponentOps[Node, A](node, extract, inject)
}
