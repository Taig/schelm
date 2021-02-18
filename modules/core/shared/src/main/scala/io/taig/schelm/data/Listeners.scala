package io.taig.schelm.data

import org.scalajs.dom.raw.Event
import cats.Monoid
import io.taig.schelm.util.Text

final case class Listeners[+F[_]](values: Map[Listener.Name, Listener.Action[F, Event]]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++[G[a] >: F[a]](listeners: Listeners[G]): Listeners[G] = Listeners(values ++ listeners.values)

  def +[G[a] >: F[a]](listener: Listener[G, Event]): Listeners[G] = Listeners(values + listener.toTuple)

  @inline
  def get(name: Listener.Name): Option[Listener.Action[F, Event]] = values.get(name)

  @inline
  def keys: Set[Listener.Name] = values.keySet

  def toList: List[Listener[F, Event]] = values.map { case (name, action) => Listener(name, action) }.toList

  override def toString: String = if (isEmpty) "[]"
  else
    s"""{
       |  ${Text.align(2)(toList.mkString(",\n"))}
       |}""".stripMargin
}

object Listeners {
  val Empty: Listeners[Nothing] = Listeners(Map.empty)

  def from[F[_]](listeners: Iterable[Listener[F, Event]]): Listeners[F] = Listeners(listeners.map(_.toTuple).toMap)

  def of[F[_]](listeners: Listener[F, Event]*): Listeners[F] = from(listeners)

  implicit def monoid[F[_]]: Monoid[Listeners[F]] = new Monoid[Listeners[F]] {
    override val empty: Listeners[F] = Empty

    override def combine(x: Listeners[F], y: Listeners[F]): Listeners[F] = x ++ y
  }
}
