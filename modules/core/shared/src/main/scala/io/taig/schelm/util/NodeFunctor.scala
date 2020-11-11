package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Node}
import simulacrum.typeclass

@typeclass
trait NodeFunctor[A] {
  def mapAttributes(fa: A)(f: Attributes => Attributes): A
}

object NodeFunctor {
  implicit def node[F[_], A]: NodeFunctor[Node[F, A]] = new NodeFunctor[Node[F, A]] {
    override def mapAttributes(node: Node[F, A])(f: Attributes => Attributes): Node[F, A] =
      node match {
        case node: Node.Element[F, A] => node.copy(tag = node.tag.copy(attributes = f(node.tag.attributes)))
        case node: Node.Fragment[A]   => node
        case node: Node.Text[F]       => node
      }
  }
}
