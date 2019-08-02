package io.taig.schelm

import cats.effect._
import cats.effect.implicits._
import cats.implicits._
import fs2.Stream

final class HtmlSchelm[F[_], Event, B](
    val dom: Dom[F, Event, B],
    manager: EventManager[F, Event]
)(implicit F: Concurrent[F])
    extends Schelm[F, Event, B] {
  override def start[State, Command](
      container: dom.Element,
      initial: State,
      render: State => F[Html[Event]],
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit] = {
    val renderer = DomRenderer(dom)

    for {
      html <- render(initial)
      node <- renderer.render(html)
      _ <- dom.appendChildren(container, node.root)
      htmls = (manager.subscription merge subscriptions)
        .evalScan(initial) { (state, event) =>
          val result = events(state, event)
          result.state.getOrElse(state).pure[F] <*
            execute(result.commands, commands, manager)
        }
        .evalMap(render)
      patcher = DomPatcher(renderer, dom)
      _ <- patch(patcher)(html, node, htmls)
    } yield ()
  }

  def patch[State, Command](
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

  def execute[Command](
      commands: List[Command],
      handler: CommandHandler[F, Command, Event],
      manager: EventManager[F, Event]
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
