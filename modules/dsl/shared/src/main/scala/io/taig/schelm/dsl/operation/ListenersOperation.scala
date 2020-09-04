package io.taig.schelm.dsl.operation

import io.taig.schelm.data.Listeners
import io.taig.schelm.dsl.data.DslWidget

abstract class ListenersOperation[F[+_], Event, Context] {
  final def set(listeners: Listeners[Event]): DslWidget[F, Event, Context] = patch(_ => listeners)

  def patch(f: Listeners[Event] => Listeners[Event]): DslWidget[F, Event, Context]
}
