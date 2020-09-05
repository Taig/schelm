package io.taig.schelm.css.data

final case class Identifier(value: Int) extends AnyVal {
  def toClass: String = "_" + Integer.toHexString(value)

  def toSelector: Selector = Selector("." + toClass)
}
