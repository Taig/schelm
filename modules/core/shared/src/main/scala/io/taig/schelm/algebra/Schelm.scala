package io.taig.schelm.algebra

abstract class Schelm[F[_], View, Event, Element] {
  def start[State, Command](
      container: Element,
      initial: State,
      render: State => View,
      handler: Handler[F, State, Event, Command]
  ): F[Unit]
}
