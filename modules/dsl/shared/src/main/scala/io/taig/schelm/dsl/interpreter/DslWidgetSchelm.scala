package io.taig.schelm.dsl.interpreter

import cats.Functor
import cats.data.Kleisli
import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.css.data._
import io.taig.schelm.css.interpreter._
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.interpreter._

object DslWidgetSchelm {
  def apply[F[_]: Concurrent, Context](
      states: StateManager[F, StyledHtml[F]],
      structurer: Renderer[F, DslWidget[F, Context], StyledHtml[F]],
      renderer: Renderer[F, StyledHtml[F], StyledHtmlReference[F]],
      attacher: Attacher[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]],
      differ: Differ[StyledHtml[F], CssHtmlDiff[F]],
      patcher: Patcher[F, StyledHtmlAttachedReference[F], CssHtmlDiff[F]]
  ): Schelm[F, DslWidget[F, Context]] =
    DomSchelm(states, structurer, renderer, attacher, differ, patcher)(a => StyledHtml(a.styles, a.html.html))

  implicit def yyy[F[_], G[_]]: Functor[λ[A => State[F, Css[Node[F, Listeners[F], A]]]]] =
    Functor[State[F, *]].compose[Css].compose[Node[F, Listeners[F], *]]

  def default[F[_], Context](states: StateManager[F, StyledHtml[F]], dom: Dom[F])(
      root: Dom.Element,
      context: Context
  )(implicit F: Concurrent[F]): Schelm[F, DslWidget[F, Context]] = {
    val asdf: Renderer[F, CssHtml[F], StyledHtml[F]] = CssHtmlRenderer[F]

    val x: Renderer[F, DslWidget[F, Context], WidgetStateCssHtml[F, Context]] = DslWidgetRenderer[F, Context]
    val y: Renderer[F, WidgetStateCssHtml[F, Context], StateCssHtml[F]] =
      WidgetRenderer.default[F, λ[α => State[F, Css[Node[F, Listeners[F], α]]]], Context](context)
    val z: Renderer[F, StateCssHtml[F], CssHtml[F]] =
      StateRenderer.root[F, λ[α => Css[Node[F, Listeners[F], α]]], StyledHtml[F]](asdf, states)

    val structurer: Renderer[F, DslWidget[F, Context], StyledHtml[F]] = x.andThen(y).andThen(z).andThen(asdf)

    val b = HtmlRenderer[F](dom)
    val c = CssRenderer[F]

    val renderer: Renderer[F, StyledHtml[F], StyledHtmlReference[F]] = Kleisli {
      case StyledHtml(styles, html) =>
        b.run(html).map(StyledHtmlReference(styles, _))
    }

    val attacher: Attacher[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]] = {
      val x = StylesheetAttacher.auto(dom)
      val y = HtmlReferenceAttacher.default(dom)(root)

      Kleisli {
        case StyledHtmlReference(styles, reference) =>
          (c.run(styles).flatMap(ss => x.flatMap(_.run(ss))), y.run(reference)).mapN { (style, reference) =>
            StyledHtmlAttachedReference(styles, reference)
          }
      }
    }

    val differ = StyledHtmlDiffer.default[F]

    val patcher: Patcher[F, StyledHtmlAttachedReference[F], CssHtmlDiff[F]] = Kleisli {
      case (reference, diff) =>
        CssHtmlPatcher.default(dom).run(reference -> diff)
    }

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
    QueueStateManager.empty[F, StyledHtml[F]].map(default[F, Context](_, dom)(root, context))
}
