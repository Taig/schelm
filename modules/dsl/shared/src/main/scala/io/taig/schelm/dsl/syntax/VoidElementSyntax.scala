package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.dsl.data.Has
import io.taig.schelm.dsl.data.Tagged.@@
import io.taig.schelm.dsl.operation.{AttributesOperation, CssOperation, ListenersOperation}

final class VoidElementSyntax[Event, Context](widget: CssWidget[Event, Context] @@ VoidElementSyntax.Tag)
    extends AttributesSyntax[Event, Context, VoidElementSyntax.Tag]
    with ListenersSyntax[Event, Context, VoidElementSyntax.Tag]
    with CssSyntax[Event, Context, VoidElementSyntax.Tag] {
  override def listeners = new ListenersOperation[Event, Context, VoidElementSyntax.Tag](widget)

  override def attributes = new AttributesOperation[Event, Context, VoidElementSyntax.Tag](widget)

  override def css = new CssOperation[Event, Context, VoidElementSyntax.Tag](widget)
}

object VoidElementSyntax {
  type Tag = Has.Attributes with Has.Listeners with Has.Css
}
