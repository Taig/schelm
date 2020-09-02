package io.taig.schelm.data

final case class Listeners[+A](values: List[Listener[A]]) extends AnyVal

object Listeners {
  val Empty: Listeners[Nothing] = Listeners(List.empty)
}
