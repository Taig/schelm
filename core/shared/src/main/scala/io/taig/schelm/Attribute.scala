package io.taig.schelm

final case class Attribute[+A](key: String, property: Property[A])
