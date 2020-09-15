package io.taig.schelm.data

final case class Listener[+Event](name: Listener.Name, action: Listener.Action[Event]) {
  def toTuple: (Listener.Name, Listener.Action[Event]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  sealed abstract class Action[+Event] extends Product with Serializable

  object Action {
    final case class Pure[Event](event: Event) extends Action[Event]
    final case class Input[Event](event: String => Event) extends Action[Event]
  }
}
