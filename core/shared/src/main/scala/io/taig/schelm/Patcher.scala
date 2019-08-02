package io.taig.schelm

abstract class Patcher[F[_], Event, Node] {
  def patch(
      node: Reference[Event, Node],
      diff: Diff[Event]
  ): F[Reference[Event, Node]]
}
