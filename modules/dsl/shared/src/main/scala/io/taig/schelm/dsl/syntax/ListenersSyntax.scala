package io.taig.schelm.dsl.syntax

import cats.implicits._
import io.taig.schelm.data.{Listener, Listeners}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.operation.ListenersOperation

trait ListenersSyntax[F[+_], Event, Context] {
  final def on(listener: Listener[Event]): DslWidget[F, Event, Context] =
    listeners.patch(_ <+> Listeners(listener))

  def listeners: ListenersOperation[F, Event, Context]
}
