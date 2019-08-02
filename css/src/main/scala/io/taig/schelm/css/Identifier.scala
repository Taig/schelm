package io.taig.schelm.css

final case class Identifier(value: Int) extends AnyVal {
  def cls: String = "_" + toString

  def selector: Selector = Selector("." + cls)

  override def toString: String = Integer.toHexString(value)
}
