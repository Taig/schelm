package io.taig.schelm.dsl.operation

import io.taig.schelm.Navigator
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Attributes
import io.taig.schelm.dsl.internal.Tagged
import io.taig.schelm.dsl.internal.Tagged.@@

final class AttributesOperation[Event, Context, Tag](widget: CssWidget[Event, Context]) {
  def set(attributes: Attributes): CssWidget[Event, Context] @@ Tag = patch(_ => attributes)

  def patch(f: Attributes => Attributes): CssWidget[Event, Context] @@ Tag =
    Tagged(Navigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]].attributes(widget, f))
}
