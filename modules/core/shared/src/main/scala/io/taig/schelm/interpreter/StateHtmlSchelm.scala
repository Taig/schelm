package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.data._

object StateHtmlSchelm {
  def apply[F[_]: Concurrent](
      states: StateManager[F, Html[F]],
      structurer: Renderer[F, StateHtml[F], Html[F]],
      renderer: Renderer[F, Html[F], HtmlReference[F]],
      attacher: Attacher[F, HtmlReference[F], HtmlAttachedReference[F]],
      differ: Differ[Html[F], HtmlDiff[F]],
      patcher: Patcher[F, HtmlAttachedReference[F], HtmlDiff[F]]
  ): Schelm[F, StateHtml[F]] =
    DomSchelm(states, structurer, renderer, attacher, differ, patcher)(_.html)

  def default[F[_]: Concurrent](states: StateManager[F, Html[F]], dom: Dom[F])(
      root: Dom.Element
  ): Schelm[F, StateHtml[F]] = {
    val structurer = StateHtmlRenderer.root(states)
    val renderer = HtmlRenderer(dom)
    val attacher = HtmlReferenceAttacher.default(dom)(root)
    val differ = HtmlDiffer[F]
    val patcher = HtmlPatcher(dom, renderer)
    StateHtmlSchelm[F](
      states,
      structurer,
      renderer,
      attacher,
      differ,
      patcher
    )
  }

  def empty[F[_]: Concurrent](dom: Dom[F])(root: Dom.Element): F[Schelm[F, StateHtml[F]]] =
    QueueStateManager.empty[F, Html[F]].map(default(_, dom)(root))

}
