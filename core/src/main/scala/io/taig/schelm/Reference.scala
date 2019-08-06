package io.taig.schelm

import cats.implicits._
import io.taig.schelm.{Element => SElement, Node => SNode, Text => SText}

sealed abstract class Reference[+Event] extends Product with Serializable {
  final def root: List[SNode] = this match {
    case Reference.Element(_, node) => List(node)
    case Reference.Fragment(component) =>
      component.children.values.flatMap(_.root)
    case Reference.Text(_, node) => List(node)
  }
}

object Reference {
  final case class Element[Event](
      component: Component.Element[Reference[Event], Event],
      node: SElement
  ) extends Reference[Event]

  final case class Fragment[Event](
      component: Component.Fragment[Reference[Event]]
  ) extends Reference[Event]

  final case class Text(component: Component.Text, node: SText)
      extends Reference[Nothing]
}
