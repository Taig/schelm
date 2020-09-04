package io.taig.schelm.dsl

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.data.DslWidget

trait ContextDsl {
  final def contextual[F[+_], Event, Context](f: Context => DslWidget[F, Event, Any]): DslWidget[F, Event, Context] =
    DslWidget(Widget.Render((context: Context) => f(context).widget))
}
