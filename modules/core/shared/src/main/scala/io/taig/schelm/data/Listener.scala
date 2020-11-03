package io.taig.schelm.data

import org.scalajs.dom.raw.Event

final case class Listener[+F[_]](name: Listener.Name, action: Listener.Action[F]) {
  def toTuple: (Listener.Name, Listener.Action[F]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  type Action[+F[_]] = Event => F[Unit]
}
