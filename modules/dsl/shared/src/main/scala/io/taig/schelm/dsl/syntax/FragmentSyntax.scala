package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.dsl.internal.Has
import io.taig.schelm.dsl.internal.Tagged.@@
import io.taig.schelm.dsl.operation.ChildrenOperation

final class FragmentSyntax[Event, Context](widget: CssWidget[Event, Context] @@ FragmentSyntax.Tag)
    extends ChildrenSyntax[Event, Context, FragmentSyntax.Tag] {
  override def children = new ChildrenOperation[Event, Context, FragmentSyntax.Tag](widget)
}

object FragmentSyntax {
  type Tag = Has.Children
}
