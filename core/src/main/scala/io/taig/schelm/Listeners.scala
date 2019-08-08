package io.taig.schelm

import cats.Applicative
import cats.data.Ior
import cats.implicits._

import scala.collection.mutable

final case class Listeners[A](values: Map[String, Action[A]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def +(listener: Listener[A]): Listeners[A] =
    updated(listener.event, listener.action)

  def updated(event: String, action: Action[A]): Listeners[A] =
    Listeners(values.updated(event, action))

  def ++(listeners: Listeners[A]): Listeners[A] =
    Listeners(values ++ listeners.values)

  def -(event: String): Listeners[A] = Listeners(values - event)

  def events: List[String] = values.keys.toList

  def traverse_[F[_]: Applicative, B](f: Listener[A] => F[B]): F[Unit] =
    toList.traverse_(f)

  def toList: List[Listener[A]] = values.toList.map {
    case (event, action) => Listener(event, action)
  }

  def zipAll(
      listeners: Listeners[A]
  ): List[(String, Ior[Action[A], Action[A]])] = {
    val result = mutable.HashMap.empty[String, Ior[Action[A], Action[A]]]

    this.values.foreach {
      case (event, action) => result.put(event, Ior.left(action))
    }

    listeners.values.foreach {
      case (key, property) =>
        val update = result
          .get(key)
          .map(_.putRight(property))
          .getOrElse(Ior.right(property))

        result.put(key, update)
    }

    result.toList
  }
}

object Listeners {
  def empty[A]: Listeners[A] = Listeners(Map.empty)

  def of[A](values: Listener[A]*): Listeners[A] = from(values)

  def from[A](values: Iterable[Listener[A]]): Listeners[A] =
    Listeners(
      Map(values.map(listener => listener.event -> listener.action).toSeq: _*)
    )
}
