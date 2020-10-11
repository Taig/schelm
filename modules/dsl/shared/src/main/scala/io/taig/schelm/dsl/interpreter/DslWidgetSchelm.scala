package io.taig.schelm.dsl.interpreter

import cats.data.Kleisli
import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.css.data._
import io.taig.schelm.css.interpreter._
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.util.Functors._
import io.taig.schelm.interpreter._
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.interpreter.{QueueEventManager, ReduxRenderer}

object DslWidgetSchelm {
  def apply[F[_]: Concurrent, Event, Context](
      states: StateManager[F, StyledHtml[F]],
      structurer: Renderer[F, DslWidget[F, Event, Context], StyledHtml[F]],
      renderer: Renderer[F, StyledHtml[F], StyledHtmlReference[F]],
      attacher: Attacher[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]],
      differ: Differ[StyledHtml[F], CssHtmlDiff[F]],
      patcher: Patcher[F, StyledHtmlAttachedReference[F], CssHtmlDiff[F]]
  ): Schelm[F, DslWidget[F, Event, Context]] =
    DomSchelm(states, structurer, renderer, attacher, differ, patcher)(a => StyledHtml(a.styles, a.html.html))

  def default[F[_], Event, Context](
      events: EventManager[F, Event],
      states: StateManager[F, StyledHtml[F]],
      dom: Dom[F]
  )(
      root: Dom.Element,
      context: Context
  )(implicit F: Concurrent[F]): Schelm[F, DslWidget[F, Event, Context]] = {
    val cssHtmlRenderer: Renderer[F, CssHtml[F], StyledHtml[F]] = CssHtmlRenderer[F]
    val htmlRenderer: Renderer[F, Html[F], HtmlReference[F]] = HtmlRenderer[F](dom)

    val structurer: Renderer[F, DslWidget[F, Event, Context], StyledHtml[F]] =
      DslWidgetRenderer[F, Event, Context]
        .andThen(
          ReduxRenderer
            .fromEventManager[F, λ[α => Widget[Context, State[F, Css[Node[F, Listeners[F], α]]]]], Event](events)
        )
        .andThen(WidgetRenderer.fromContext[F, λ[α => State[F, Css[Node[F, Listeners[F], α]]]], Context](context))
        .andThen(StateRenderer.root[F, λ[α => Css[Node[F, Listeners[F], α]]], StyledHtml[F]](cssHtmlRenderer, states))
        .andThen(cssHtmlRenderer)

    val renderer: Renderer[F, StyledHtml[F], StyledHtmlReference[F]] =
      Kleisli {
        case StyledHtml(styles, html) =>
          htmlRenderer.run(html).map(StyledHtmlReference(styles, _))
      }

    val attacher: Attacher[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]] = {
      val x = StylesheetAttacher.auto(dom)
      val y = HtmlReferenceAttacher.default(dom)(root)
      val c = CssRenderer[F]

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

  def empty[F[_]: Concurrent, Event, Context](
      dom: Dom[F]
  )(root: Dom.Element, context: Context): F[Schelm[F, DslWidget[F, Event, Context]]] =
    (QueueEventManager.unbounded[F, Event], QueueStateManager.empty[F, StyledHtml[F]])
      .mapN(default[F, Event, Context](_, _, dom)(root, context))
}
