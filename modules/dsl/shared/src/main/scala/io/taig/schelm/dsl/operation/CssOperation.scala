package io.taig.schelm.dsl.operation

import io.taig.schelm.css.CssNavigator
import io.taig.schelm.css.data.{CssWidget, Style}
import io.taig.schelm.dsl.data.Tagged
import io.taig.schelm.dsl.data.Tagged.@@

final class CssOperation[Event, Context, Tag](widget: CssWidget[Event, Context]) {
  def set(style: Style): CssWidget[Event, Context] @@ Tag = patch(_ => style)

  def patch(f: Style => Style): CssWidget[Event, Context] @@ Tag =
    Tagged(CssNavigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]].style(widget, f))
}
