package io.taig.schelm.css.data

import io.taig.schelm.data.{Listeners, Node, State}
import org.scalajs.dom.raw.Event

final case class ComponentCssHtml[F[_]](css: State[F, Css[Node[F, Listeners[F], ComponentCssHtml[F]]]]) extends AnyVal
