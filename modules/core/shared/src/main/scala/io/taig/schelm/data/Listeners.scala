package io.taig.schelm.data

import cats.MonoidK

final case class Listeners[+A](values: Map[Listener.Name, Listener.Action[A]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[B >: A](listeners: Listeners[B]): Listeners[B] = Listeners(values ++ listeners.values)

  def +[B >: A](listener: Listener[B]): Listeners[B] = Listeners(values + listener.toTuple)

  def toList: List[Listener[A]] = values.map { case (name, action) => Listener(name, action) }.toList
}

object Listeners {
  val Empty: Listeners[Nothing] = Listeners(Map.empty)

  def from[A](listeners: Iterable[Listener[A]]): Listeners[A] = Listeners(listeners.map(_.toTuple).toMap)

  def of[A](listeners: Listener[A]*): Listeners[A] = from(listeners)

  implicit val monoidK: MonoidK[Listeners] = new MonoidK[Listeners] {
    override def empty[A]: Listeners[A] = Empty

    override def combineK[A](x: Listeners[A], y: Listeners[A]): Listeners[A] = x ++ y
  }
}
