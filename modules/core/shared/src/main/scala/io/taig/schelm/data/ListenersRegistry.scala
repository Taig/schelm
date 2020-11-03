package io.taig.schelm.data

final case class ListenersRegistry[F[_]](values: Map[Path, Listeners[F]]) extends AnyVal
