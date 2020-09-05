package io.taig.schelm.css.data

import cats.effect.ConcurrentEffect
import io.taig.schelm.algebra.Schelm
import org.scalajs.dom

object JsCssSchelm {
  def default[F[_]: ConcurrentEffect, Event]: F[Schelm[F, CssHtml[Event], Event]] = ???
}
