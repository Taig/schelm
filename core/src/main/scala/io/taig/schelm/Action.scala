package io.taig.schelm

sealed abstract class Action[+A] extends Product with Serializable

object Action {
  final case class Pure[A](event: A) extends Action[A]
  final case class Input[A](event: String => A) extends Action[A]
}
