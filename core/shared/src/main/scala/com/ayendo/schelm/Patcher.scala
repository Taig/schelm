package com.ayendo.schelm

abstract class Patcher[F[_], A, B] {
  def patch(node: Node[A, B], diff: Diff[A]): F[Node[A, B]]
}
