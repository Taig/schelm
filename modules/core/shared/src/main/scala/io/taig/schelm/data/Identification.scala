package io.taig.schelm.data

import cats.data.Chain
import cats.implicits._

final case class Identification(values: Chain[Identifier]) extends AnyVal {
  def ++(identification: Identification): Identification = Identification(values ++ identification.values)

  def /(identifier: Identifier): Identification = Identification(values.append(identifier))

  def last: Option[Identifier] = values.lastOption
}

object Identification {
  val Empty: Identification = Identification(Chain.empty)

  object / {
    def unapply[T](identification: Identification): Option[(Identifier, Identification)] =
      identification.values.uncons.map(_.map(apply))
  }
}
