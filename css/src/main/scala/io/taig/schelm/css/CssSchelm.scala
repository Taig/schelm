package io.taig.schelm.css

import cats.effect.ConcurrentEffect
import io.taig.schelm._

object CssSchelm {
  def apply[F[_]: ConcurrentEffect, A](
      manager: EventManager[F, A],
      dom: Dom[F, A],
      globals: Stylesheet
  ): Schelm[F, A, StyledHtml[A], StyledReference[A], StyledHtmlDiff[A]] = {
    val renderer = HtmlRenderer(dom)
    Schelm(
      dom,
      manager,
      StyledHtmlRenderer(renderer),
      StyledReferenceAttacher(dom, globals),
      StyledHtmlDiffer[A],
      StyledReferencePatcher(dom, renderer, globals)
    )
  }
}
