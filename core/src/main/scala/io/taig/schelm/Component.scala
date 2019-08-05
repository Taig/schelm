package io.taig.schelm

import cats.Eval

sealed abstract class Component[+A, +Event] extends Product with Serializable

object Component {
  final case class Element[A, Event](
      name: String,
      attributes: Attributes[Event],
      children: Children[A]
  ) extends Component[A, Event]

  final case class Fragment[A](children: Children[A])
      extends Component[A, Nothing]

  final case class Lazy[A](component: Eval[A], hash: Int)
      extends Component[A, Nothing]

  final case class Text(value: String) extends Component[Nothing, Nothing]


}
