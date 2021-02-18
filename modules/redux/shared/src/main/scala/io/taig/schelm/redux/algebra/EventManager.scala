package io.taig.schelm.redux.algebra

import cats.effect.kernel.Spawn
import fs2.Stream

abstract class EventManager[F[_], A] {
  def submit(event: A): F[Unit]

  def subscription: Stream[F, A]
}

object EventManager {
  def noop[F[_], A](implicit F: Spawn[F]): EventManager[F, A] = new EventManager[F, A] {
    override def submit(event: A): F[Unit] = F.unit

    override def subscription: Stream[F, A] = Stream.eval(F.never)
  }
}
