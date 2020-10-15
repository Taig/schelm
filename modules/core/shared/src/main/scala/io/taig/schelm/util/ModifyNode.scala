package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Node}
import simulacrum.typeclass

@typeclass
trait ModifyNode[A] {
  def modifyAttributes(fa: A)(f: Attributes => Attributes): A
}

object ModifyNode {
  implicit def node[F[_], Listeners, A]: ModifyNode[Node[F, Listeners, A]] = new ModifyNode[Node[F, Listeners, A]] {
    override def modifyAttributes(fa: Node[F, Listeners, A])(f: Attributes => Attributes): Node[F, Listeners, A] =
      fa match {
        case node: Node.Element[F, Listeners, A] => node.copy(tag = node.tag.copy(attributes = f(node.tag.attributes)))
        case node: Node.Fragment[A]              => node
        case node: Node.Text[F, Listeners]       => node
      }
  }
}
