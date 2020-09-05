package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.Concurrent
import io.taig.schelm.algebra.{EventManager, Schelm}
import io.taig.schelm.data.Html
import org.jsoup.nodes.{Element => JElement}

object JvmHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, Event]: Schelm[F, Html[Event], Event] =
    HtmlSchelm.default(new JElement("div"), EventManager.noop[F, Event], JsoupDom.default[F, Event])
}
