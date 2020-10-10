package io.taig.schelm.dsl

import io.taig.schelm.data.Widget
import io.taig.schelm.dsl.data.DslWidget

trait ContextDsl {
  final def contextual[F[_], Context](f: Context => DslWidget[F, Context]): DslWidget[F, Context] =
   DslWidget.Pure(Widget.Render((context: Context) => f(context).apply(context)))
}
