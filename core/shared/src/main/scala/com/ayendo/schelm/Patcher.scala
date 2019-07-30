package com.ayendo.schelm

abstract class Patcher[F[_], A, B] {
  def patch(node: B, diff: Diff[A]): F[B]
}
