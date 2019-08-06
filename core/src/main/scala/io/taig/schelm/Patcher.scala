package io.taig.schelm

abstract class Patcher[F[_], A, B] {
  def patch(reference: A, diff: B, path: Path): F[A]
}
