package io.taig.schelm.algebra

abstract class Attacher[F[_], A, B] {
  def attach(value: A): F[B]
}
