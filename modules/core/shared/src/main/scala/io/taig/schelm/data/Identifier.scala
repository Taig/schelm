package io.taig.schelm.data

import cats.Order

final case class Identifier(value: String) extends AnyVal

object Identifier {
  implicit val order: Order[Identifier] = Order.by(_.value)
}
