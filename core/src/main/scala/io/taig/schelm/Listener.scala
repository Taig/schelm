package io.taig.schelm

final case class Listener[A](event: String, action: Action[A])
