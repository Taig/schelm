package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Listener
import io.taig.schelm.dsl.internal.Tagged.@@
import io.taig.schelm.dsl.operation.ListenersOperation

trait ListenersSyntax[Event, Context, Tag] {
  final def on(listener: Listener[Event]): CssWidget[Event, Context] @@ Tag = listeners.patch(_ + listener)

  def listeners: ListenersOperation[Event, Context, Tag]
}
