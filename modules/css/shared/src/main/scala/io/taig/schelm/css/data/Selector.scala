package io.taig.schelm.css.data

final case class Selector(value: String) extends AnyVal {
  def +(modifier: Modifier): Selector = Selector(value + modifier.value)
}
