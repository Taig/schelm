package io.taig.schelm.css

final case class Declaration(key: String, value: String) {
  override def toString: String = s"$key: $value"
}
