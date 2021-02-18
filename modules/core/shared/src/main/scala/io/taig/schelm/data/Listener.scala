package io.taig.schelm.data

import org.scalajs.dom.raw.Event

final case class Listener[+F[_], -A <: Event](name: Listener.Name, action: Listener.Action[F, A]) {
  def toTuple: (Listener.Name, Listener.Action[F, A]) = (name, action)

  override def toString: String = s"${name.value}: $action"
}

object Listener {
  final case class Name(value: String) extends AnyVal

  type Action[+F[_], -A <: Event] = A => F[Unit]
}
