package com.ayendo.schelm

sealed abstract class Key extends Product with Serializable {
  override final def toString: String = this match {
    case Key.Identifier(value) => value
    case Key.Index(value)      => String.valueOf(value)
  }
}

object Key {
  final case class Identifier(value: String) extends Key
  final case class Index(value: Int) extends Key
}
