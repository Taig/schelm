package io.taig.schelm.data

import cats.Traverse
import io.taig.schelm.algebra.Dom

sealed abstract class Reference[+Event, +A] extends Product with Serializable {
  final def toNodes: List[Dom.Node] = this match {
    case Reference.Element(_, dom) => List(dom)
    case Reference.Fragment(node)  => node.children.indexed.flatMap(_.toNodes)
    case Reference.Text(_, dom)    => List(dom)
  }
}

object Reference {
  final case class Element[+Event, +A](node: Node.Element[Event, Reference[Event, A]], dom: Dom.Element)
      extends Reference[Event, A]

  final case class Fragment[+Event, +A](node: Node.Fragment[Reference[Event, A]]) extends Reference[Event, A]

  final case class Text[+Event](node: Node.Text[Event], dom: Dom.Text) extends Reference[Event, Nothing]

  implicit def traverse[Structure]: Traverse[Reference[Structure, *]] = ???
}
