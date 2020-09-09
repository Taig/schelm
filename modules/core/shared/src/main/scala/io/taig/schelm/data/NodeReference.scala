package io.taig.schelm.data

sealed abstract class NodeReference[+Event, +Element, +Text, +A] extends Product with Serializable

object NodeReference {
  final case class Element[+Event, Dom, +A](node: Component.Element[Event, A], dom: Dom)
      extends NodeReference[Event, Dom, Nothing, A]

  final case class Fragment[+Event, +A](node: Component.Fragment[A]) extends NodeReference[Nothing, Nothing, Nothing, A]

  final case class Text[+Event, Dom](node: Component.Text[Event], dom: Dom)
      extends NodeReference[Event, Nothing, Dom, Nothing]
}
