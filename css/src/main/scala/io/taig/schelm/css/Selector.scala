package io.taig.schelm.css

final case class Selector(value: String) extends AnyVal {
  def +(modifier: Modifier): Selector = Selector(value + modifier.toString)

  override def toString: String = value
}
