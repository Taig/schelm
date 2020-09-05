package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.Concurrent
import cats.effect.implicits._
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.data.{Html, Patcher, Result}

final class DomSchelm[F[_]: Parallel, View, Event, Structure, Element, Diff](
    manager: EventManager[F, Event],
    renderer: Renderer[F, View, Structure],
    attacher: Attacher[F, Structure, Element],
    differ: Differ[View, Diff],
    patcher: Patcher[F, Structure, Diff]
)(implicit F: Concurrent[F])
    extends Schelm[F, View, Event, Element] {
  override def start[State, Command](
      container: Element,
      initial: State,
      render: State => View,
      handler: Handler[F, State, Event, Command]
  ): F[Unit] = {
    val view = render(initial)

    for {
      structure <- renderer.render(view)
      _ <- attacher.attach(container, structure)
      _ <- manager.subscription
        .evalMapAccumulate((initial, view)) {
          case ((state, previous), event) =>
            val update = handler.event(state, event)

            update match {
              case Result(None, Nil)                           => ((state, previous), none[Diff]).pure[F]
              case Result(Some(state), Nil) if update == state => ((state, previous), none[Diff]).pure[F]
              case Result(Some(state), Nil) =>
                val next = render(state)
                ((state, next), differ.diff(previous, next)).pure[F]
              case Result(None, commands) =>
                publish(handler.command, commands).start *> ((state, previous), none[Diff]).pure[F]
              case Result(Some(state), commands) =>
                val next = render(state)
                publish(handler.command, commands).start *> ((state, next), differ.diff(previous, next)).pure[F]
            }
        }
        .collect { case (_, Some(diff)) => diff }
        .evalMap(patcher.patch(structure, _))
        .compile
        .drain
        .start
    } yield ()
  }

  def publish[Command](handler: Command => F[Option[Event]], commands: List[Command]): F[Unit] =
    commands.parTraverse_ { command =>
      handler.apply(command).flatMap(_.traverse_(manager.submit)).handleErrorWith { throwable =>
        F.delay {
          System.err.println("Failed to handle command")
          throwable.printStackTrace(System.err)
        }
      }
    }
}

object DomSchelm {
  def apply[F[_]: Concurrent: Parallel, View, Event, Structure, Element, Diff](
      manager: EventManager[F, Event],
      renderer: Renderer[F, View, Structure],
      attacher: Attacher[F, Structure, Element],
      differ: Differ[View, Diff],
      patcher: Patcher[F, Structure, Diff]
  ): Schelm[F, View, Event, Element] = new DomSchelm(manager, renderer, attacher, differ, patcher)

  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Node, Element, Diff](
      manager: EventManager[F, Event],
      dom: Dom.Aux[F, Event, Node, Element, _]
  ): Schelm[F, Html[Event], Event, Element] = {
    val renderer = HtmlRenderer(dom)
    val attacher = HtmlAttacher(dom)
    val differ = HtmlDiffer[Event]
    val patcher = HtmlPatcher(dom, renderer)
    DomSchelm(manager, renderer, attacher, differ, patcher)
  }
}
