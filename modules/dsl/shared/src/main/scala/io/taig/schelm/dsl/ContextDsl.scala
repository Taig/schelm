package io.taig.schelm.dsl

import io.taig.schelm.dsl.data.DslWidget

trait ContextDsl {
  final def contextual[Context](f: Context => DslWidget[Any]): DslWidget[Context] =
    // CssWidget(Widget.Render((context: Context) => f(context).widget))
    ???
}
