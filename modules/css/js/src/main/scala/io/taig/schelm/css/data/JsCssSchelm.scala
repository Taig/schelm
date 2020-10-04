package io.taig.schelm.css.data

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Schelm}
import io.taig.schelm.css.interpreter.CssHtmlSchelm

object JsCssSchelm {
  def default[F[_]: ConcurrentEffect: Parallel](
      dom: Dom[F]
  )(main: Dom.Element): F[Schelm[F, CssHtml[F]]] = ???
  //QueueEventManager.unbounded[F, Event].flatMap(manager => CssHtmlSchelm.default(dom)(main, manager))
}
