package io.taig.schelm.dsl.syntax

import io.taig.schelm.data
import io.taig.schelm.data.{Listener, Listeners}
import org.scalajs.dom.raw.{Event, EventTarget}

trait listener {
  implicit class ListenerNameOps(name: data.Listener.Name) {
    def :=[F[_], E <: Event, T <: EventTarget](action: Listener.Action[F, E, T]): Listener[F, E, T] = Listener(name, action)
  }

  @inline
  def listeners[F[_]](values: Listener[F, Event, EventTarget]*): Listeners[F] = Listeners.from(values)

  object effect {
    @inline
    def apply[F[_], E <: Event, T <: EventTarget](f: (E, T) => F[Unit]): Listener.Action[F, E, T] =
      Listener.Action.Effect(f)

    @inline
    def default[F[_]](f: (Event, EventTarget) => F[Unit]): Listener.Action[F, Event, EventTarget] =
      Listener.Action.Effect(f)

    @inline
    def event[F[_], E <: Event](f: E => F[Unit]): Listener.Action[F, E, EventTarget] =
      Listener.Action.Effect[F, E, EventTarget]((event, _) => f(event))

    @inline
    def target[F[_], T <: EventTarget](f: T => F[Unit]): Listener.Action[F, Event, T] =
      Listener.Action.Effect[F, Event, T]((_, target) => f(target))

    @inline
    def run[F[_]](f: F[Unit]): Listener.Action[F, Event, EventTarget] =
      Listener.Action.Effect[F, Event, EventTarget]((_, _) => f)

    val noop: Listener.Action[Nothing, Event, EventTarget] = Listener.Action.Noop
  }


  val change: Listener.Name = Listener.Name("change")
  val click: Listener.Name = Listener.Name("click")
  val input: Listener.Name = Listener.Name("input")
}

object listener extends listener
