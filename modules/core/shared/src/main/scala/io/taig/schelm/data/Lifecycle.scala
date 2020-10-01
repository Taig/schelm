package io.taig.schelm.data

import cats.Applicative
import cats.effect.Resource
import io.taig.schelm.algebra.Dom

object Lifecycle {
  type Element[F[_]] = Lifecycle[F, Dom.Element]

  type Text[F[_]] = Lifecycle[F, Dom.Text]

  def noop[F[_]: Applicative]: Lifecycle[F, Any] = _ => Resource.pure[F, Unit](())
}
