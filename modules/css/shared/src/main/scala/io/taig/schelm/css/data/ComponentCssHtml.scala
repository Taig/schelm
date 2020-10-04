package io.taig.schelm.css.data

import io.taig.schelm.data.{Listeners, Node, State}

final case class ComponentCssHtml[F[_]](css: State[F, CssNode[Node[F, Listeners[F], ComponentCssHtml[F]]]])
    extends AnyVal
