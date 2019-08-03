package io.taig.schelm.css

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm._
import io.taig.schelm.css.internal.StyleHelpers

final class StyledReferencePatcher[F[_]: Monad, Event, Node](
    dom: Dom[F, Event, Node],
    patcher: Patcher[F, Reference[Event, Node], Diff[Event]]
) extends Patcher[F, StyledReference[Event, Node], StyledDiff[Event]] {
  override def patch(
      reference: StyledReference[Event, Node],
      diff: StyledDiff[Event]
  ): F[StyledReference[Event, Node]] =
    for {
      style <- StyleHelpers.getOrCreateStyleElement(dom)
      _ <- dom.innerHtml(style, s"\n${diff.stylesheet}\n")
      ref <- patcher.patch(reference.reference, diff.diff)
    } yield StyledReference(ref, reference.stylesheet)
}

object StyledReferencePatcher {
  def apply[F[_]: Sync, Event, Node](
      renderer: Renderer[F, Html[Event], Reference[Event, Node]],
      dom: Dom[F, Event, Node]
  ): Patcher[F, StyledReference[Event, Node], StyledDiff[Event]] =
    new StyledReferencePatcher[F, Event, Node](
      dom,
      ReferencePatcher[F, Event, Node](renderer, dom)
    )
}
