package io.taig.schelm.algebra

abstract class Schelm[F[_], View, Event] {
  def start[State, Command](
      initial: State,
      render: State => View
  ): F[Unit]
}
