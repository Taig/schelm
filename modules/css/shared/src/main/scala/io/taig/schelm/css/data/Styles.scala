package io.taig.schelm.css.data

final case class Styles(values: List[Style]) extends AnyVal

object Styles {
  val Empty: Styles = Styles(List.empty)
}
