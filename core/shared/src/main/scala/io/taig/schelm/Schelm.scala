package io.taig.schelm

import cats._
import cats.effect.{Concurrent, ConcurrentEffect}
import cats.effect.implicits._
import cats.implicits._
import fs2.Stream

final class Schelm[F[_], Event, Node, Component, Reference, Diff](
    manager: EventManager[F, Event],
    renderer: Renderer[F, Component, Reference],
    attacher: Attacher[F, Node, Reference],
    differ: Differ[Component, Diff],
    patcher: Patcher[F, Reference, Diff]
)(implicit F: ConcurrentEffect[F]) {
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
      reference <- renderer.render(component)
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
  def apply[F[_]: ConcurrentEffect, Event, Node, Component, Reference, C](
      manager: EventManager[F, Event],
      renderer: Renderer[F, Component, Reference],
      attacher: Attacher[F, Node, Reference],
      differ: Differ[Component, C],
      patcher: Patcher[F, Reference, C]
  ): Schelm[F, Event, Node, Component, Reference, C] =
    new Schelm[F, Event, Node, Component, Reference, C](
      manager,
      renderer,
      attacher,
      differ,
      patcher
    )
}
