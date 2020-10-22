package io.taig.schelm.dsl.syntax

import io.taig.schelm.data
import io.taig.schelm.data.Listener
import org.scalajs.dom.raw.Event

trait listener {
  implicit class ListenerNameOps(name: data.Listener.Name) {
    def :=[F[_]](f: Event => F[Unit]): Listener[F] = Listener(name, Listener.Action.Effect(f))
  }

  val change: Listener.Name = Listener.Name("change")
  val click: Listener.Name = Listener.Name("click")
  val input: Listener.Name = Listener.Name("input")
}

object listener extends listener
