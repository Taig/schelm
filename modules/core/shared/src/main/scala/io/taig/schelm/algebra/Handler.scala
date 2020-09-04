package io.taig.schelm.algebra

import io.taig.schelm.data.Result

abstract class Handler[F[_], State, Event, Command] {
  def command(value: Command): F[Option[Event]]

  def event(state: State, event: Event): Result[State, Command]
}
