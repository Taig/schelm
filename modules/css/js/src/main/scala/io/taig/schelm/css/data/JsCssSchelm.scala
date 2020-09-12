package io.taig.schelm.css.data

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Schelm}
import io.taig.schelm.css.interpreter.CssHtmlSchelm
import io.taig.schelm.interpreter.QueueEventManager

object JsCssSchelm {
  def default[F[_]: ConcurrentEffect: Parallel, Event](dom: Dom)(main: dom.Element): F[Schelm[F, CssHtml[F], Event]] =
    QueueEventManager.unbounded[F, Event].flatMap { manager => CssHtmlSchelm.default(dom)(main, manager) }
}
