package io.taig.schelm.algebra

abstract class Patcher[F[_], Structure, Diff] {
  def patch(structure: Structure, diff: Diff): F[Structure]
}
