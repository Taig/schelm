package io.taig.schelm.data

final case class Tag[+F[_]](name: Tag.Name, attributes: Attributes, listeners: Listeners[F])

object Tag {
  final case class Name(value: String) extends AnyVal
}
