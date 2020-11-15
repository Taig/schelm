package io.taig.schelm.data

final case class ListenerRegistry[+F[_]](values: Map[Path, Listeners[F]]) extends AnyVal

object ListenerRegistry {
  def from[F[_]](values: Iterable[(Path, Listeners[F])]): ListenerRegistry[F] = ListenerRegistry(values.toMap)

  def of[F[_]](values: (Path, Listeners[F])*): ListenerRegistry[F] = from(values)

  val Empty: ListenerRegistry[Nothing] = ListenerRegistry(Map.empty)
}
