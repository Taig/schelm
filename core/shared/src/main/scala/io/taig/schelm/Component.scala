package io.taig.schelm

import cats.implicits._

sealed abstract class Component[+A, +B] extends Product with Serializable

object Component {
  final case class Element[A, B](
      name: String,
      attributes: Attributes[B],
      children: Children[A]
  ) extends Component[A, B]

  final case class Fragment[A, B](children: Children[A]) extends Component[A, B]

  final case class Text(value: String) extends Component[Nothing, Nothing]
}
