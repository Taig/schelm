package io.taig.schelm.dsl

import cats.data.Kleisli
import cats.effect.implicits._
import cats.effect.{Concurrent, Resource}
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.css.data._
import io.taig.schelm.css.interpreter._
import io.taig.schelm.data.{Listeners, Node, State, Widget}
import io.taig.schelm.dsl.data.DslNode
import io.taig.schelm.dsl.interpreter.DslWidgetRenderer
import io.taig.schelm.dsl.util.Functors._
import io.taig.schelm.interpreter._
import io.taig.schelm.util.PathTraversal.ops._
import io.taig.schelm.redux.algebra.EventManager
import io.taig.schelm.redux.interpreter.{QueueEventManager, ReduxRenderer}

final class Schelm[F[_], Event, Context](
    states: StateManager[F, StyledHtml[F]],
    events: EventManager[F, Event],
    structurer: Renderer[Kleisli[F, Context, *], DslNode[F, Event, Context], StyledHtml[F]],
    renderer: Renderer[F, StyledHtml[F], StyledHtmlReference[F]],
    attacher: Attacher[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]],
    differ: Differ[StyledHtml[F], CssHtmlDiff[F]],
    patcher: Patcher[F, StyledHtmlAttachedReference[F], CssHtmlDiff[F]]
)(implicit F: Concurrent[F]) {
  def start[A](initial: A)(
      context: A => Context,
      render: A => DslNode[F, Event, Context]
  ): Resource[F, Unit] =
    for {
      reference <- Resource.liftF {
        structurer
          .mapK(Kleisli.applyK(context(initial)))
          .andThen(renderer)
          .run(render(initial))
          .flatMap(attacher.run)
      }
      _ <- process(reference).background
    } yield ()

  private def process(reference: StyledHtmlAttachedReference[F]): F[Unit] =
    states.subscription
      .map(_.asLeft)
      .merge(events.subscription.map(_.asRight))
      .evalScan(reference) {
        case (reference, Left(state)) =>
          reference.modify[F](state.path) { reference =>
            differ
              .diff(StyledHtml(reference.styles, reference.html.html), state.structure)
              .traverse(diff => patcher.run((reference, diff)))
              .map(_.getOrElse(reference))
          }
        case (reference, Right(event)) =>
          println("It's event time!")
          ???
      }
      .compile
      .drain
      .onError { throwable =>
        F.delay {
          System.err.println("Failed to apply state update")
          throwable.printStackTrace()
        }
      }
}

object Schelm {
  def default[F[_]: Concurrent, Event, Context](dom: Dom[F])(root: Dom.Element): F[Schelm[F, Event, Context]] =
    for {
      // format: off
      states <- QueueStateManager.empty[F, StyledHtml[F]]
      events <- QueueEventManager.unbounded[F, Event]
      cssHtmlRenderer = CssHtmlRenderer[F]
      htmlRenderer = HtmlRenderer[F](dom)
      cssRenderer = CssRenderer[F]
      stylesheetAttacher <- StylesheetAttacher.auto(dom)
      styleAttacher = stylesheetAttacher.compose(cssRenderer.run)
      htmlAttacher = HtmlReferenceAttacher.default(dom)(root)
      cssHtmlPatcher = CssHtmlPatcher.default(dom)
      structurer = DslWidgetRenderer[F, Event, Context].mapK(Kleisli.liftK[F, Context])
        .andThen(ReduxRenderer.fromEventManager[F, λ[A => Widget[Context, State[F, Css[Node[F, Listeners[F], A]]]]], Event](events).mapK(Kleisli.liftK[F, Context]))
        .andThen(WidgetRenderer[F, λ[A => State[F, Css[Node[F, Listeners[F], A]]]], Context])
        .andThen(StateRenderer.root[F, λ[A => Css[Node[F, Listeners[F], A]]], StyledHtml[F]](cssHtmlRenderer, states).mapK(Kleisli.liftK[F, Context]))
        .andThen(cssHtmlRenderer.mapK(Kleisli.liftK[F, Context]))
      renderer = Kleisli[F, StyledHtml[F], StyledHtmlReference[F]] {
        case StyledHtml(styles, html) => htmlRenderer.run(html).map(StyledHtmlReference(styles, _))
      }
      attacher = Kleisli[F, StyledHtmlReference[F], StyledHtmlAttachedReference[F]] {
        case StyledHtmlReference(styles, reference) =>
          styleAttacher.run(styles) *> htmlAttacher.run(reference).map(StyledHtmlAttachedReference(styles, _))
      }
      differ = StyledHtmlDiffer.default[F]
      patcher = Patcher[F, StyledHtmlAttachedReference[F], CssHtmlDiff[F]] { (reference, diff) =>
        cssHtmlPatcher.run(reference -> diff)
      }
      // format: on
    } yield new Schelm(states, events, structurer, renderer, attacher, differ, patcher)
}
