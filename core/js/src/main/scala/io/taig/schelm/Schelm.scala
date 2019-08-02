package io.taig.schelm

import cats.effect._
import cats.effect.implicits._
import cats.implicits._
import cats.{Eq, Parallel}
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.internal.EffectHelpers

object Schelm {
  def start[F[_]: ConcurrentEffect, G[_], State, Event: Eq, Command, B](
      id: String,
      dom: Dom[F, Event, B],
      queue: Queue[F, Event]
  )(
      initial: State,
      render: State => F[Html[Event]],
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event] = Stream.empty
  )(implicit P: Parallel[F, G]): F[Unit] =
    for {
      container <- dom
        .getElementById(id)
        .flatMap(
          EffectHelpers.get[F, dom.Element](_, s"No element with id $id found")
        )
      renderer = DomRenderer(dom)
      patcher = DomPatcher(renderer, dom)
      html <- render(initial)
      node <- renderer.render(html)
      _ <- dom.appendChildren(container, node.root)
      htmls = (queue.dequeue merge subscriptions)
        .evalScan(initial) { (state, event) =>
          val result = events(state, event)
          result.state.getOrElse(state).pure[F] <*
            execute(result.commands, commands, queue)
        }
        .evalMap(render)
      _ <- start(patcher)(html, node, htmls)
    } yield ()

  def start[F[_]: Concurrent, State, Event: Eq, Command, B](
      patcher: Patcher[F, Event, B]
  )(
      initial: Html[Event],
      node: Node[Event, B],
      htmls: Stream[F, Html[Event]]
  ): F[Unit] =
    htmls
      .evalScan((initial, node)) {
        case (data @ (previous, node), next) =>
          Differ.diff(previous, next).fold(data.pure[F]) { diff =>
            patcher.patch(node, diff).map((next, _))
          }
      }
      .compile
      .drain
      .handleErrorWith { throwable =>
        Concurrent[F].delay(throwable.printStackTrace())
      }
      .start
      .void

  private def execute[F[_], G[_], B, C](
      commands: List[C],
      handler: CommandHandler[F, C, B],
      queue: Queue[F, B]
  )(implicit F: Concurrent[F], P: Parallel[F, G]): F[Unit] =
    if (commands.isEmpty) F.unit
    else
      commands
        .parTraverse_ { command =>
          handler(command).flatMap {
            case Some(event) => queue.enqueue1(event)
            case None        => F.unit
          }
        }
        .start
        .void
}
