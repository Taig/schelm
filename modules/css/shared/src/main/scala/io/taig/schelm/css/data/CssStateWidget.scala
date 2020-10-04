package io.taig.schelm.css.data

import io.taig.schelm.data.{Listeners, Node, State, Widget}

final case class CssStateWidget[F[_], -Context](
    widget: Widget[Context, State[F, CssNode[Node[F, Listeners[F], CssStateWidget[F, Context]]]]]
) extends AnyVal
