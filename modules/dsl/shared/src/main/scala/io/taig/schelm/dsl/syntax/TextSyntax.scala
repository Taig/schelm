package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.dsl.data.Has
import io.taig.schelm.dsl.data.Tagged.@@
import io.taig.schelm.dsl.operation.ListenersOperation

final class TextSyntax[Event, Context](widget: CssWidget[Event, Context] @@ TextSyntax.Tag)
    extends ListenersSyntax[Event, Context, TextSyntax.Tag] {
  override def listeners = new ListenersOperation[Event, Context, TextSyntax.Tag](widget)
}

object TextSyntax {
  type Tag = Has.Listeners
}
