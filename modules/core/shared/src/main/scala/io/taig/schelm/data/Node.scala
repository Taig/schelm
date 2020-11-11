package io.taig.schelm.data

import cats.Traverse

sealed abstract class Node[+F[_], +A] extends Product with Serializable {
  final override def toString: String = this match {
    case Node.Element(tag, variant, lifecycle) =>
      s"Node.Element(tag = $tag, variant = $variant, lifecycle = $lifecycle)"
    case Node.Fragment(children) => s"Node.Fragment(children = $children)"
    case Node.Text(value, listeners, lifecycle) =>
      s"Node.Text(value = $value, listeners = $listeners, lifecycle = $lifecycle)"
  }
}

object Node {
  final case class Element[+F[_], +A](
      tag: Tag[F],
      variant: Element.Variant[A],
      lifecycle: Lifecycle.Element[F]
  ) extends Node[F, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Variant[+A] extends Product with Serializable

    object Variant {
      final case class Normal[+A](children: Children[A]) extends Variant[A]
      final case object Void extends Variant[Nothing]
    }

    implicit def traverse[F[_]]: Traverse[Element[F, *]] = new NodeElementInstances[F]
  }

  final case class Fragment[+A](children: Children[A]) extends Node[Nothing, A]

  object Fragment {
    implicit val traverse: Traverse[Fragment] = NodeFragmentInstances
  }

  final case class Text[+F[_]](
      value: String,
      listeners: Listeners[F],
      lifecycle: Lifecycle.Text[F]
  ) extends Node[F, Nothing]

  implicit def traverse[F[_]]: Traverse[Node[F, *]] = new NodeInstances[F]
}
