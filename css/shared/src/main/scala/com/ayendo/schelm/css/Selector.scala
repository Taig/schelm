package com.ayendo.schelm.css

final case class Selector(value: String) extends AnyVal {
  def +(modifier: Modifier): Selector = Selector(value + modifier.render)

  def render: String = value
}
