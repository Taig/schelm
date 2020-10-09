package io.taig.schelm.dsl.interpreter

import cats.Functor
import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.css.data.{
  Css,
  CssHtml,
  CssHtmlDiff,
  StateCssHtml,
  StyledHtml,
  StylesheetHtmlAttachedReference,
  StylesheetHtmlReference,
  WidgetStateCssHtml
}
import io.taig.schelm.css.interpreter.{CssHtmlRenderer, CssRenderer}
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter.{DomSchelm, HtmlRenderer, QueueStateManager, StateRenderer, WidgetRenderer}
import io.taig.schelm.util.NodeTraverse

object DslWidgetSchelm {
  def apply[F[_]: Concurrent, Context](
      states: StateManager[F, CssHtml[F]],
      structurer: Renderer[F, DslWidget[F, Context], CssHtml[F]],
      renderer: Renderer[F, CssHtml[F], StylesheetHtmlReference[F]],
      attacher: Attacher[F, StylesheetHtmlReference[F], StylesheetHtmlAttachedReference[F]],
      differ: Differ[CssHtml[F], CssHtmlDiff[F]],
      patcher: Patcher[F, StylesheetHtmlAttachedReference[F], CssHtmlDiff[F]]
  ): Schelm[F, DslWidget[F, Context]] =
    DomSchelm(states, structurer, renderer, attacher, differ, patcher)(???)(???, ???)

  implicit def yyy[F[_], G[_]]: Functor[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]] =
    Functor[State[F, *]].compose[Css].compose[Node[F, Listeners[F], *]]

  def default[F[_], Context](states: StateManager[F, CssHtml[F]], dom: Dom[F])(
      root: Dom.Element,
      context: Context
  )(implicit F: Concurrent[F]): Schelm[F, DslWidget[F, Context]] = {
    val x: Renderer[F, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] = DslWidgetRenderer[F, Context]
    val y: Renderer[F, WidgetStateCssHtml[F, Context], StateCssHtml[F]] =
      WidgetRenderer.default[F, λ[A => State[F, Css[Node[F, Listeners[F], A]]]], Context](context)
    val z: Renderer[F, StateCssHtml[F], CssHtml[F]] =
      StateRenderer.root[F, λ[A => Css[Node[F, Listeners[F], A]]]](states)

    val structurer = x.andThen(y).andThen(z)

    val a = CssHtmlRenderer[F]
    val b = HtmlRenderer[F](dom)
    val c = CssRenderer[F]

    val renderer = a.andThen {
      case StyledHtml(styles, html: Html[F]) =>
        (c.run(styles), b.run(html)).mapN(StylesheetHtmlReference.apply)
    }

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

  def empty[F[_]: Concurrent, Context](
      dom: Dom[F]
  )(root: Dom.Element, context: Context): F[Schelm[F, DslWidget[F, Context]]] =
    QueueStateManager.empty[F, CssHtml[F]].map(default[F, Context](_, dom)(root, context))
}
