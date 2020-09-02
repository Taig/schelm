package io.taig.schelm.data

final case class Attributes(values: List[Attribute]) extends AnyVal

object Attributes {
  val Empty: Attributes = Attributes(List.empty)
}
