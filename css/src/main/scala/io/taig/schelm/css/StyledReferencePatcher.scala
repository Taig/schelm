package io.taig.schelm.css

import cats.Functor
import cats.implicits._
import cats.effect.Sync
import io.taig.schelm._

final class StyledReferencePatcher[F[_]: Functor, Event, Node](
    patcher: Patcher[F, Reference[Event, Node], Diff[Event]]
) extends Patcher[F, StyledReference[Event, Node], StyledDiff[Event]] {
  override def patch(
      node: StyledReference[Event, Node],
      diff: StyledDiff[Event]
  ): F[StyledReference[Event, Node]] =
    // TODO Patch stylesheeet
    patcher.patch(node.reference, diff.diff).map { ref =>
      StyledReference(ref, node.stylesheet)
    }
}

object StyledReferencePatcher {
  def apply[F[_]: Sync, Event, Node](
      renderer: Renderer[F, Html[Event], Reference[Event, Node]],
      dom: Dom[F, Event, Node]
  ): Patcher[F, StyledReference[Event, Node], StyledDiff[Event]] =
    new StyledReferencePatcher[F, Event, Node](
      ReferencePatcher[F, Event, Node](renderer, dom)
    )
}
