package io.taig.schelm.css

import cats.effect.Sync
import io.taig.schelm.{Diff, Patcher, Reference}

final class StyledReferencePatcher[F[_], Event, Node](
    patcher: Patcher[F, Reference[Event, Node], Diff[Event]]
) extends Patcher[F, StyledReference[Event, Node], StyledDiff[Event]] {
  override def patch(
      node: StyledReference[Event, Node],
      diff: StyledDiff[Event]
  ): F[StyledReference[Event, Node]] = ???
}
