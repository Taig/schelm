package io.taig.schelm.dsl

import io.taig.schelm.data.Listener

trait ListenerDsl {
  val click: Listener.Name = Listener.Name("click")
}
