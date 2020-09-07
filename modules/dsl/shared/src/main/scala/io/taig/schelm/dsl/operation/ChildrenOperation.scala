package io.taig.schelm.dsl.operation

import io.taig.schelm.Navigator
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Children
import io.taig.schelm.dsl.internal.Tagged
import io.taig.schelm.dsl.internal.Tagged.@@

final class ChildrenOperation[Event, Context, Tag](widget: CssWidget[Event, Context]) {
  def set(children: Children[CssWidget[Event, Context]]): CssWidget[Event, Context] @@ Tag = patch(_ => children)

  def patch(
      f: Children[CssWidget[Event, Context]] => Children[CssWidget[Event, Context]]
  ): CssWidget[Event, Context] @@ Tag =
    Tagged(Navigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]].children(widget, f))
}
