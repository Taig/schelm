package io.taig.schelm.dsl

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.data.DslWidget.toWidget

trait ContextDsl {
  final def contextual[Event, Context](f: Context => DslWidget[Event, Any]): DslWidget[Event, Context] =
    DslWidget.Pure(Widget.Render((context: Context) => toWidget(f(context))))
}
