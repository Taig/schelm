package io.taig.schelm.data

import org.scalajs.dom.raw.{Event, EventTarget}

final case class Listener[+F[_], +E <: Event, +T <: EventTarget](
    name: Listener.Name,
    action: Listener.Action[F, E, T]
) {
  def toTuple: (Listener.Name, Listener.Action[F, E, T]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  sealed trait Action[+F[_], +E <: Event, +T <: EventTarget] extends Product with Serializable

  object Action {
    final case object Noop extends Action[Nothing, Nothing, Nothing]
    final case class Effect[F[_], E <: Event, T <: EventTarget](f: (E, T) => F[Unit]) extends Action[F, E, T]
  }
}
