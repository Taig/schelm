package io.taig.schelm

import cats.effect.ConcurrentEffect

object HtmlSchelm {
  def apply[F[_]: ConcurrentEffect, A](
      manager: EventManager[F, A],
      dom: Dom[F, A]
  ): Schelm[F, A, Html[A], Reference[A], HtmlDiff[
    A
  ]] = {
    val renderer = HtmlRenderer(dom)
    Schelm(
      dom,
      manager,
      renderer,
      ReferenceAttacher(dom),
      HtmlDiffer[A],
      ReferencePatcher(dom, renderer)
    )
  }
}
