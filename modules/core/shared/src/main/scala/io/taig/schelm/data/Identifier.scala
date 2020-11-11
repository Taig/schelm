package io.taig.schelm.data

import cats.Show

final case class Identifier(value: String) extends AnyVal

object Identifier {
  implicit val show: Show[Identifier] = value => s"'$value'"
}
