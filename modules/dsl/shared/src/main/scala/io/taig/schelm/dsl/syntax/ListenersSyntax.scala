package io.taig.schelm.dsl.syntax

import cats.implicits._
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.{Listener, Listeners}
import io.taig.schelm.dsl.data.Tagged.@@
import io.taig.schelm.dsl.operation.ListenersOperation

trait ListenersSyntax[Event, Context, Tag] {
  final def on(listener: Listener[Event]): CssWidget[Event, Context] @@ Tag =
    listeners.patch(_ <+> Listeners(listener))

  def listeners: ListenersOperation[Event, Context, Tag]
}
