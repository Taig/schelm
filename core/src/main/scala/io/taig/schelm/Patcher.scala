package io.taig.schelm

abstract class Patcher[F[_], A, B] {
  def patch(node: A, diff: B): F[A]
}
