package io.taig.schelm

import cats.effect.implicits._
import cats.effect.{Concurrent, ConcurrentEffect}
import cats.implicits._
import fs2.Stream
import io.taig.schelm.internal.EffectHelpers

final class Schelm[F[_], Event, Node, Component, Reference, Diff](
    dom: Dom[F, Event, Node],
    manager: EventManager[F, Event],
    renderer: Renderer[F, Component, Reference],
    attacher: Attacher[F, Node, Reference],
    differ: Differ[Component, Diff],
    patcher: Patcher[F, Reference, Diff]
)(implicit F: ConcurrentEffect[F]) {
  def start[State, Command](
      id: String,
      initial: State,
      render: State => Component,
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit] =
    dom
      .getElementById(id)
      .flatMap(EffectHelpers.get[F](_, s"No element exists for id '$id'"))
      .flatMap(start(_, initial, render, events, commands, subscriptions))

  def start[State, Command](
      container: Node,
      initial: State,
      render: State => Component,
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit] = {
    val component = render(initial)

    for {
      reference <- renderer.render(component, Path.Empty)
      _ <- attacher.attach(container, reference)
      htmls = (manager.subscription merge subscriptions)
        .evalScan(initial) { (state, event) =>
          val result = events(state, event)
          result.state.getOrElse(state).pure[F] <*
            execute(result.commands, commands)
        }
        .map(render)
      _ <- patch(patcher, component, reference, htmls)
    } yield ()
  }

  def patch[State, Command](
      patcher: Patcher[F, Reference, Diff],
      initial: Component,
      reference: Reference,
      htmls: Stream[F, Component]
  ): F[Unit] =
    htmls
      .evalScan((initial, reference)) {
        case (data @ (previous, reference), next) =>
          differ.diff(previous, next).fold(data.pure[F]) { diff =>
            patcher.patch(reference, diff).map((next, _))
          }
      }
      .compile
      .drain
      .handleErrorWith { throwable =>
        Concurrent[F].delay(throwable.printStackTrace())
      }
      .start
      .void

  def execute[Command](
      commands: List[Command],
      handler: CommandHandler[F, Command, Event]
  ): F[Unit] =
    commands
      .traverse_ { command =>
        handler(command).flatMap {
          case Some(event) => manager.submit(event)
          case None        => F.unit
        }
      }
      .start
      .void
}

object Schelm {
  def apply[F[_]: ConcurrentEffect, Event, Node, Component, Reference, Diff](
      dom: Dom[F, Event, Node],
      manager: EventManager[F, Event],
      renderer: Renderer[F, Component, Reference],
      attacher: Attacher[F, Node, Reference],
      differ: Differ[Component, Diff],
      patcher: Patcher[F, Reference, Diff]
  ): Schelm[F, Event, Node, Component, Reference, Diff] =
    new Schelm[F, Event, Node, Component, Reference, Diff](
      dom,
      manager,
      renderer,
      attacher,
      differ,
      patcher
    )
}
