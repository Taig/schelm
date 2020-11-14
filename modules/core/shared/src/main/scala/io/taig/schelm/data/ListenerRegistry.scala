package io.taig.schelm.data

import scala.collection.mutable

final case class ListenerRegistry[+F[_]](values: Map[Path, Listeners[F]]) extends AnyVal

object ListenerRegistry {
  def from[F[_]](values: Iterable[(Path, Listeners[F])]): ListenerRegistry[F] = ListenerRegistry(values.toMap)

  def of[F[_]](values: (Path, Listeners[F])*): ListenerRegistry[F] = from(values)

  def newBuilder[F[_]]: mutable.Builder[(Path, Listeners[F]), ListenerRegistry[F]] =
    Map.newBuilder[Path, Listeners[F]].mapResult(apply)

  val Empty: ListenerRegistry[Nothing] = ListenerRegistry(Map.empty)
}
