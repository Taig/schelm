package io.taig.schelm.dsl

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Widget

trait ContextDsl {
  final def contextual[F[_], Context](f: Context => CssWidget[F, Any]): CssWidget[F, Context] =
    CssWidget(Widget.Render((context: Context) => f(context).widget))
}
