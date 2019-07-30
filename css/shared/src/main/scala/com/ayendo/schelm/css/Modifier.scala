package com.ayendo.schelm.css

final case class Modifier(value: String) extends AnyVal {
  def render: String = value
}
