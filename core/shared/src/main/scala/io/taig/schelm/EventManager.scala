package io.taig.schelm

import cats.Applicative
import cats.effect.implicits._
import cats.effect.{ConcurrentEffect, Effect, IO}
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue

abstract class EventManager[F[_], Event] {
  def submit(event: Event): F[Unit]

  def submitUnsafe(event: Event): Unit

  def subscription: Stream[F, Event]
}

object EventManager {
  def noop[F[_], Event](implicit F: Applicative[F]): EventManager[F, Event] =
    new EventManager[F, Event] {
      override def submit(event: Event): F[Unit] = F.unit

      override def submitUnsafe(event: Event): Unit = ()

      override val subscription: Stream[F, Event] = Stream.empty
    }

  def queue[F[_]: Effect, Event](
      queue: Queue[F, Event]
  ): EventManager[F, Event] =
    new EventManager[F, Event] {
      override def submit(event: Event): F[Unit] = queue.enqueue1(event)

      override def submitUnsafe(event: Event): Unit =
        submit(event).runAsync(_ => IO.unit).unsafeRunSync()

      override def subscription: Stream[F, Event] = queue.dequeue
    }

  def unbounded[F[_]: ConcurrentEffect, Event]: F[EventManager[F, Event]] =
    Queue.unbounded[F, Event].map(queue[F, Event])
}
