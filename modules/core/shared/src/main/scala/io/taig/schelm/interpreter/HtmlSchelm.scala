package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Schelm}
import io.taig.schelm.data.Html

object HtmlSchelm {
  def default[F[_]: Concurrent](dom: Dom[F])(root: Dom.Element): F[Schelm[F, Html[F]]] =
    QueueStateManager.empty[F, Html[F]].map { states =>
      val renderer = HtmlRenderer[F](dom)
      val attacher = HtmlReferenceAttacher.default(dom)(root)
      val differ = HtmlDiffer[F]
      val patcher = HtmlPatcher(dom, renderer)

//      DomSchelm(states, renderer, attacher, differ, patcher)
      ???
    }
}
