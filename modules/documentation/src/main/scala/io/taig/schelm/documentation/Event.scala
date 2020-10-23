package io.taig.schelm.documentation

sealed abstract class Event extends Product with Serializable

object Event {
  final case class TextChanged(value: String) extends Event
}
