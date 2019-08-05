package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm.Patcher

final class StylesheetPatcher[F[_]: Applicative]
    extends Patcher[F, Stylesheet, StylesheetDiff] {
  override def patch(
      stylesheet: Stylesheet,
      diff: StylesheetDiff
  ): F[Stylesheet] = {
    stylesheet.pure[F]
  }
}

object StylesheetPatcher {
  def apply[F[_]: Applicative]: Patcher[F, Stylesheet, StylesheetDiff] =
    new StylesheetPatcher[F]
}
