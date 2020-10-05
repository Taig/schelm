package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Differ, Dom, Patcher, Renderer, Schelm, StateManager}
import io.taig.schelm.data.{Html, HtmlAttachedReference, HtmlDiff, HtmlReference}

object HtmlSchelm {
  def apply[F[_]: Concurrent](
      states: StateManager[F, Html[F]],
      renderer: Renderer[F, Html[F], HtmlReference[F]],
      attacher: Attacher[F, HtmlReference[F], HtmlAttachedReference[F]],
      differ: Differ[Html[F], HtmlDiff[F]],
      patcher: Patcher[F, HtmlAttachedReference[F], HtmlDiff[F]]
  ): Schelm[F, Html[F]] =
    DomSchelm[F, Html[F], HtmlReference[F], HtmlAttachedReference[F], HtmlDiff[F]](
      states,
      renderer,
      attacher,
      differ,
      patcher
    )

  def default[F[_]: Concurrent](states: StateManager[F, Html[F]], dom: Dom[F])(
      root: Dom.Element
  ): Schelm[F, Html[F]] = {
    val renderer = HtmlRenderer[F](dom)
    val attacher = HtmlReferenceAttacher.default(dom)(root)
    val differ = HtmlDiffer[F]
    val patcher = HtmlPatcher(dom, renderer)

    HtmlSchelm(states, renderer, attacher, differ, patcher)
  }

  def empty[F[_]: Concurrent](dom: Dom[F])(root: Dom.Element): F[Schelm[F, Html[F]]] =
    QueueStateManager.empty[F, Html[F]].map(default(_, dom)(root))
}
