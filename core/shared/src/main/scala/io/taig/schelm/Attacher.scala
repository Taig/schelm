package io.taig.schelm

abstract class Attacher[F[_], A] {
  def attach(value: A): F[Unit]
}
