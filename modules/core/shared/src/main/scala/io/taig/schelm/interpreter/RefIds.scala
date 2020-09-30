package io.taig.schelm.interpreter

import cats.Applicative
import cats.effect.Sync
import cats.effect.concurrent.Ref
import io.taig.schelm.algebra.Ids
import io.taig.schelm.data.Identifier
import cats.implicits._

final class RefIds[F[_]: Applicative](cursor: Ref[F, Int]) extends Ids[F] {
  override val next: F[Identifier] = cursor.updateAndGet(_ + 1).map(Identifier.apply)
}

object RefIds {
  def apply[F[_]: Applicative](cursor: Ref[F, Int]): Ids[F] = new RefIds[F](cursor)

  def default[F[_]: Sync]: F[Ids[F]] = Ref[F].of(0).map(RefIds[F])
}
