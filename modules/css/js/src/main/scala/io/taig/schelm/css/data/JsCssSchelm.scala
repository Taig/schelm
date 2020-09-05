package io.taig.schelm.css.data

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.css.interpreter.CssHtmlSchelm
import io.taig.schelm.interpreter.{BrowserDom, QueueEventManager}
import org.scalajs.dom

object JsCssSchelm {
  def default[F[_]: ConcurrentEffect: Parallel, Event](
      main: dom.Element,
      style: dom.Element
  ): F[Schelm[F, CssHtml[Event], Event]] =
    QueueEventManager.unbounded[F, Event].map { manager =>
      val dom = BrowserDom(manager)
      CssHtmlSchelm.default(main, style, manager, dom)
    }
}
