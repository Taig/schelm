package io.taig.schelm.algebra

import io.taig.schelm.data.Result

abstract class Handler[F[_], State, Event, Command] {
  def command: Command => F[Option[Event]]

  def event: (State, Event) => Result[State, Command]
}
