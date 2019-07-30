package com.ayendo.schelm.css

import cats.Applicative
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._

final class StylesRegistry[F[_]: Applicative](
    globals: Stylesheet,
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
      globals ++ style.toStylesheet(selectors)
    }
  }
}

object StylesRegistry {
  def apply[F[_]: Concurrent](globals: Stylesheet): F[StylesRegistry[F]] =
    Ref[F].of(Styles.Empty).map(new StylesRegistry[F](globals, _))
}
