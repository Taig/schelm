package com.ayendo.schelm.css

final case class Declaration(key: String, value: String) {
  def render: String = s"$key: $value"
}
