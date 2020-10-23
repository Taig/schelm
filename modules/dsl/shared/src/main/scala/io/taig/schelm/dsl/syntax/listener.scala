package io.taig.schelm.dsl.syntax

import io.taig.schelm.data
import io.taig.schelm.data.Listener
import org.scalajs.dom.raw.{Event, EventTarget}

trait listener {
  implicit class ListenerNameOps(name: data.Listener.Name) {
    def :=[F[_], E <: Event, T <: EventTarget](action: Listener.Action[F, E, T]): Listener[F, E, T] =
      Listener(name, action)

    def :=[F[_], E <: Event, T <: EventTarget](f: (E, T) => F[Unit]): Listener[F, E, T] =
      Listener(name, Listener.Action.Effect(f))
  }

  type DomEvent = Event

  val noop: Listener.Action[Nothing, Nothing, Nothing] = Listener.Action.Noop

  val change: Listener.Name = Listener.Name("change")
  val click: Listener.Name = Listener.Name("click")
  val input: Listener.Name = Listener.Name("input")
}

object listener extends listener
