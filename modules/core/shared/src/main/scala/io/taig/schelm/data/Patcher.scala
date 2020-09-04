package io.taig.schelm.data

abstract class Patcher[F[_], Structure, Diff] {
  def patch(structure: Structure, diff: Diff): F[Unit]
}
