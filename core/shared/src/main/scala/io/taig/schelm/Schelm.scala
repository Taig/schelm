package io.taig.schelm

import cats.MonadError
import cats.implicits._
import fs2.Stream
import io.taig.schelm.internal.EffectHelpers

abstract class Schelm[F[_], Event, Node] {
  val dom: Dom[F, Event, Node]

  final def start[State, Command](
      id: String,
      initial: State,
      render: State => F[Html[Event]],
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event] = Stream.empty
  )(implicit F: MonadError[F, Throwable]): F[Unit] =
    dom
      .getElementById(id)
      .flatMap(EffectHelpers.get[F](_, s"No element with id $id found"))
      .flatMap(start(_, initial, render, events, commands, subscriptions))

  def start[State, Command](
      container: dom.Element,
      initial: State,
      render: State => F[Html[Event]],
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit]
}
