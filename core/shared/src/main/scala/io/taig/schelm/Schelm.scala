package io.taig.schelm

import cats._
import cats.implicits._
import fs2.Stream

final class Schelm[F[_]: Monad, Event, Node, A, B](
    renderer: Renderer[F, A, B],
    attacher: Attacher[F, Node, B]
) {
//  final def start[State, Command](
//      id: String,
//      initial: State,
//      render: State => Html[Event],
//      events: EventHandler[State, Event, Command],
//      commands: CommandHandler[F, Command, Event],
//      subscriptions: Stream[F, Event] = Stream.empty
//  )(implicit F: MonadError[F, Throwable]): F[Unit] =
//    dom
//      .getElementById(id)
//      .flatMap(EffectHelpers.get[F](_, s"No element with id $id found"))
//      .flatMap(start(_, initial, render, events, commands, subscriptions))

  def start[State, Command](
      container: Node,
      initial: State,
      render: State => A,
      events: EventHandler[State, Event, Command],
//      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit] = {
    val a = render(initial)
    renderer.render(a).flatMap { b =>
      attacher.attach(container, b)
    }
  }
}

object Schelm {
  def apply[F[_]: Monad, Event, Node, A, B](
      renderer: Renderer[F, A, B],
      attacher: Attacher[F, Node, B]
  ): Schelm[F, Event, Node, A, B] =
    new Schelm[F, Event, Node, A, B](renderer, attacher)
}
