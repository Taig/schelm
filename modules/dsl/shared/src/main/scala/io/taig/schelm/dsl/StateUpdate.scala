package io.taig.schelm.dsl

final class StateUpdate[F[_], A](val current: A, update: (A => A) => F[Unit]) {
  def modify(f: A => A): F[Unit] = update(f)

  def set(value: A): F[Unit] = update(_ => value)
}
