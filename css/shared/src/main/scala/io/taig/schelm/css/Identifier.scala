package io.taig.schelm.css

final case class Identifier(value: Int) extends AnyVal {
  def render: String = Integer.toHexString(value)

  def cls: String = "_" + render

  def selector: Selector = Selector("." + cls)
}
