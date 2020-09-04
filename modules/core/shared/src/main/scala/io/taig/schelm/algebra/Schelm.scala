package io.taig.schelm.algebra

abstract class Schelm[F[_], View, Event, Element] {
  def start[State](
      container: Element,
      initial: State,
      render: State => View,
      events: (State, Event) => State
  ): F[Unit]
}
