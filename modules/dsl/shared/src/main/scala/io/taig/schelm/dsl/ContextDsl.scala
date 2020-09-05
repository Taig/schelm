package io.taig.schelm.dsl

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Widget

trait ContextDsl {
  final def contextual[Event, Context](f: Context => CssWidget[Event, Any]): CssWidget[Event, Context] =
    CssWidget(Widget.Render((context: Context) => f(context).widget))
}
