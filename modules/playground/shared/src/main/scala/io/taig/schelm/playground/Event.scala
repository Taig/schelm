package io.taig.schelm.playground

sealed abstract class Event extends Product with Serializable

object Event {
  final case object LeClick extends Event
}
