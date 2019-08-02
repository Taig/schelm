package io.taig.schelm

abstract class Patcher[F[_], A, B] {
  def patch(node: Reference[A, B], diff: Diff[A]): F[Reference[A, B]]
}
