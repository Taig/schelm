package io.taig.schelm.data

import org.scalajs.dom.raw.Event

final case class Listener[+F[_]](name: Listener.Name, action: Listener.Action[F]) {
  def toTuple: (Listener.Name, Listener.Action[F]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  sealed trait Action[+F[_]] extends Product with Serializable

  object Action {
    final case object Noop extends Action[Nothing]
    final case class Effect[F[_]](f: Event => F[Unit]) extends Action[F]

    def apply[F[_]](f: Event => F[Unit]): Action[F] = Effect(f)
  }
}
