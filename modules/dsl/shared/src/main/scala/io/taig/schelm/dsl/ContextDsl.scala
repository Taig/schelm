package io.taig.schelm.dsl

trait ContextDsl {
  final def contextual[Context](f: Context => DslWidget[Any]): DslWidget[Context] =
    // CssWidget(Widget.Render((context: Context) => f(context).widget))
    ???
}
