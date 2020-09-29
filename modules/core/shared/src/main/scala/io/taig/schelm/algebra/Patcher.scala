package io.taig.schelm.algebra

import cats.Applicative

abstract class Patcher[F[_], Structure, Diff] {
  def patch(structure: Structure, diff: Diff): F[Unit]
}

object Patcher {
  def noop[F[_], Structure, Diff](implicit F: Applicative[F]): Patcher[F, Structure, Diff] = (_, _) => F.unit
}
