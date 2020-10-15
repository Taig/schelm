package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class Listener[+F[_]](name: Listener.Name, action: Listener.Action[F]) {
  def toTuple: (Listener.Name, Listener.Action[F]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  type Action[+F[_]] = Dom.Event => F[Unit]
}
