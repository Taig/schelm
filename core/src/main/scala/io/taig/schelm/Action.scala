package io.taig.schelm

sealed abstract class Action[Event] extends Product with Serializable

object Action {
  final case class Pure[Event](event: Event) extends Action[Event]
  final case class Input[Event](event: String => Event) extends Action[Event]
}
