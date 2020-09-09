package io.taig.schelm.data

final case class Listener(name: Listener.Name, action: Listener.Action) {
  def toTuple: (Listener.Name, Listener.Action) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  sealed abstract class Action extends Product with Serializable

  object Action {
    final case class Pure(event: Event) extends Action
    final case class Input(event: String => Event) extends Action
  }
}
