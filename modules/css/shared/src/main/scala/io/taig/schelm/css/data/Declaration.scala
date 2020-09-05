package io.taig.schelm.css.data

final case class Declaration(name: Declaration.Name, value: Declaration.Value)

object Declaration {
  final case class Name(value: String) extends AnyVal
  final case class Value(value: String) extends AnyVal
}
