package io.taig.schelm.dsl

import io.taig.schelm.{Action, Listener}

trait ListenersDsl {
  def on[A](event: String, action: Action[A]): Listener[A] =
    Listener(event, action)

  // format: off
  def onClick[A](value: A): Listener[A] = on("click", Action.Pure(value))
  def onSubmit[A](value: A): Listener[A] = on("submit", Action.Pure(value))
  // format: on
}
