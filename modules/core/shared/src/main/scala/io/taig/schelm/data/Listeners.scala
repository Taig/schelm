package io.taig.schelm.data

import cats.MonoidK

final case class Listeners[+A](values: List[Listener[A]]) extends AnyVal

object Listeners {
  val Empty: Listeners[Nothing] = Listeners(List.empty)

  def apply[A](listeners: Listener[A]*): Listeners[A] = Listeners(listeners.toList)

  implicit val monoidK: MonoidK[Listeners] = new MonoidK[Listeners] {
    override def empty[A]: Listeners[A] = Empty

    override def combineK[A](x: Listeners[A], y: Listeners[A]): Listeners[A] =
      Listeners(x.values ++ y.values)
  }
}
