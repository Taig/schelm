package io.taig.schelm.dsl

import io.taig.schelm.data.Widget

trait ContextDsl {
  final def contextual[F[+_], Event, Context](f: Context => DslWidget[F, Event, Any]): DslWidget[F, Event, Context] =
    Widget.Render((context: Context) => f(context))
}
