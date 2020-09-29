package io.taig.schelm.data

final case class Tag[F[_]](name: String, attributes: Attributes, listeners: Listeners[F])
