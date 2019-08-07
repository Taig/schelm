package io.taig.schelm.css

import cats.Applicative
import cats.implicits._
import io.taig.schelm._

final class StylesheetPatcher[F[_]: Applicative, A](dom: Dom[F, A]) {
  def patch(
      style: Element,
      stylesheet: Stylesheet,
      diff: StylesheetDiff
  ): F[Stylesheet] = {
    val update = stylesheet ++ diff.value
    if (stylesheet != update)
      dom.innerHtml(style, update.toString) *> update.pure[F]
    else stylesheet.pure[F]
  }
}

object StylesheetPatcher {
  def apply[F[_]: Applicative, A](dom: Dom[F, A]) =
    new StylesheetPatcher[F, A](dom)
}
