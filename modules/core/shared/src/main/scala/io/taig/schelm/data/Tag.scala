package io.taig.schelm.data

final case class Tag[Event](name: String, attributes: Attributes, listeners: Listeners[Event])
