package io.taig.schelm.dsl.syntax

import io.taig.schelm.algebra.Dom
import io.taig.schelm.data
import io.taig.schelm.data.Listener

trait listener {
  implicit class ListenerNameOps(name: data.Listener.Name) {
    def :=[F[_]](f: Dom.Event => F[Unit]): Listener[F] = Listener(name, f)
  }

  val click: Listener.Name = Listener.Name("click")
}

object listener extends listener
