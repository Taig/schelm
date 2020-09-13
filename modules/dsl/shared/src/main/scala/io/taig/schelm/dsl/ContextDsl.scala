package io.taig.schelm.dsl

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Widget

trait ContextDsl {
  final def contextual[F[_], Context](f: Context => DslWidget[F, Any]): DslWidget[F, Any] =
    ??? // CssWidget(Widget.Render((context: Context) => f(context).widget))
}
