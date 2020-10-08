package io.taig.schelm.css.data

import io.taig.schelm.data.{Listeners, Node, State}

final case class StateCssHtml[F[_]](state: State[F, CssNode[Node[F, Listeners[F], StateCssHtml[F]]]]) extends AnyVal
