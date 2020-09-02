package io.taig.schelm.data

final case class Listener[+A](name: Listener.Name, action: Listener.Action[A])

object Listener {
  final case class Name(value: String) extends AnyVal

  sealed abstract class Action[+A] extends Product with Serializable

  object Action {
    final case class Pure[A](event: A) extends Action[A]
    final case class Input[A](event: String => A) extends Action[A]
  }
}
