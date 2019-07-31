package com.ayendo.schelm.css

import cats.Applicative
import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._

final class CssRegistry[F[_]: Applicative](
    registry: Ref[F, Styles]
) {
  def register(styles: Styles): F[List[Identifier]] =
    if (styles.isEmpty) List.empty[Identifier].pure[F]
    else registry.update(_ ++ styles).as(styles.map(identify))

  def identify(style: Style): Identifier = Identifier(style.hashCode)

  def reset: F[Unit] = registry.set(Styles.Empty)

  def snapshot: F[Stylesheet] = registry.get.map { styles =>
    styles.foldMap { style =>
      val selectors = Selectors.one(identify(style).selector)
      style.toStylesheet(selectors)
    }
  }
}

object CssRegistry {
  def apply[F[_]: Sync]: F[CssRegistry[F]] =
    Ref[F].of(Styles.Empty).map(new CssRegistry[F](_))
}
