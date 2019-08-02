package io.taig.schelm.css

final case class Modifier(value: String) extends AnyVal {
  override def toString: String = value
}
