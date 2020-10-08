package io.taig.schelm.css.data

import io.taig.schelm.data.{Listeners, Node, State, Widget}

final case class WidgetStateCssHtml[F[_], -Context](
    widget: Widget[Context, State[F, CssNode[Node[F, Listeners[F], WidgetStateCssHtml[F, Context]]]]]
) extends AnyVal
