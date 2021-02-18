package io.taig.schelm.css.data

final case class Declaration(name: Declaration.Name, value: Declaration.Value) {
  override def toString: String = s"${name.value} = ${value.value}"
}

object Declaration {
  final case class Name(value: String) extends AnyVal

  final case class Value(value: String) extends AnyVal {
    def concat(value: Value, divider: String): Value = Value(this.value + divider + value.value)
  }
}
