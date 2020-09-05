package io.taig.schelm.data

import cats.Applicative

abstract class Patcher[F[_], Structure, Diff] {
  def patch(structure: Structure, diff: Diff): F[Unit]
}

object Patcher {
  def noop[F[_]: Applicative, Structure, Diff]: Patcher[F, Structure, Diff] = new Patcher[F, Structure, Diff] {
    override def patch(structure: Structure, diff: Diff): F[Unit] = Applicative[F].unit
  }
}
