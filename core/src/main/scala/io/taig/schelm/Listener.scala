package io.taig.schelm

final case class Listener[Event](event: String, action: Action[Event])
