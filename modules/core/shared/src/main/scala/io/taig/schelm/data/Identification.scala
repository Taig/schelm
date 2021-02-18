package io.taig.schelm.data

import cats.data.Chain
import cats.implicits._

/** A DOM location description that exclusively consists of `Identifier`s */
final case class Identification(values: Chain[Identifier]) extends AnyVal {
  def ++(identification: Identification): Identification = Identification(values ++ identification.values)

  def /(identifier: Identifier): Identification = Identification(values.append(identifier))

  def last: Option[Identifier] = values.lastOption
}

object Identification {
  val Root: Identification = Identification(Chain.empty)

  def from(identifiers: Iterable[Identifier]): Identification = Identification(Chain.fromSeq(identifiers.toSeq))

  def of(identifiers: Identifier*): Identification = from(identifiers)

  object / {
    def unapply[T](identification: Identification): Option[(Identifier, Identification)] =
      identification.values.uncons.map(_.map(Identification(_)))
  }
}
