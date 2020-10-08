package io.taig.schelm.dsl.interpreter

import cats.arrow.FunctionK
import cats.effect.Concurrent
import cats.implicits._
import cats.{Applicative, Id}
import io.taig.schelm.algebra._
import io.taig.schelm.css.data.{CssHtml, CssHtmlDiff, CssNode, StateCssHtml, WidgetStateCssHtml}
import io.taig.schelm.data.Widget.⟳
import io.taig.schelm.data.{Fix, HtmlAttachedReference, HtmlReference, Listeners, Node, State, Widget}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.{DomSchelm, QueueStateManager, WidgetRenderer}

object DslWidgetSchelm {
  def apply[F[_]: Concurrent, Context](
      states: StateManager[F, CssHtml[F]],
      structurer: Renderer[F, DslWidget[F, Context], CssHtml[F]],
      renderer: Renderer[F, CssHtml[F], HtmlReference[F]],
      attacher: Attacher[F, HtmlReference[F], HtmlAttachedReference[F]],
      differ: Differ[CssHtml[F], CssHtmlDiff[F]],
      patcher: Patcher[F, HtmlAttachedReference[F], CssHtmlDiff[F]]
  ): Schelm[F, DslWidget[F, Context]] =
    DomSchelm(states, structurer, renderer, attacher, differ, patcher)(???)

  def default[F[_], Context](states: StateManager[F, CssHtml[F]], dom: Dom[F])(
      root: Dom.Element
  )(implicit F: Concurrent[F]): Schelm[F, DslWidget[F, Context]] = {
    val context: Context = ???
    val x: Renderer[F, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] = DslWidgetRenderer[F, Context]
//    val y: Renderer[F, WidgetStateCssHtml[F, Context], State[F, CssNode[Node[F, Listeners[F], Fix[λ[A => State[F, CssNode[Node[F, Listeners[F], A]]]]]]]]] = WidgetRenderer.default[F, λ[A => State[F, CssNode[Node[F, Listeners[F], A]]]], Context](context)

    val structurer = ??? // CssHtmlRenderer[F].mapK(λ[FunctionK[Id, F]](F.pure(_)))
    val renderer = ??? // HtmlRenderer(dom)
    val attacher = ??? // HtmlReferenceAttacher.default(dom)(root)
    val differ = ??? // HtmlDiffer[F]
    val patcher = ??? // HtmlPatcher(dom, renderer)
    DslWidgetSchelm(
      states,
      structurer,
      renderer,
      attacher,
      differ,
      patcher
    )
  }

  def empty[F[_]: Concurrent, Context](dom: Dom[F])(root: Dom.Element): F[Schelm[F, DslWidget[F, Context]]] =
    QueueStateManager.empty[F, CssHtml[F]].map(default[F, Context](_, dom)(root))
}
