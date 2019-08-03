package io.taig.schelm.css

import cats.effect.ConcurrentEffect
import io.taig.schelm.{Dom, EventManager, HtmlRenderer, Schelm}

object WidgetSchelm {
  def apply[F[_]: ConcurrentEffect, Event, Node](
      manager: EventManager[F, Event],
      dom: Dom[F, Event, Node]
  ): Schelm[F, Event, Node, StyledHtml[Event], StyledReference[Event, Node], StyledDiff[
    Event
  ]] =
    Schelm(
      dom,
      manager,
      StyledHtmlRenderer(dom),
      StyledReferenceAttacher(dom),
      StyledHtmlDiffer[Event],
      StyledReferencePatcher(HtmlRenderer(dom), dom)
    )
}
