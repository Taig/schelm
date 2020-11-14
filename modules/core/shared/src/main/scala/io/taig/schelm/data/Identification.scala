package io.taig.schelm.data

import cats.Show
import cats.data.Chain
import cats.implicits._

/** A DOM location description that exclusively consists of `Identifier`s */
final case class Identification(values: Chain[String]) extends AnyVal {
  def ++(identification: Identification): Identification = Identification(values ++ identification.values)

  def /(identifier: Key.Identifier): Identification = Identification(values.append(identifier.value))

  def last: Option[Key.Identifier] = values.lastOption.map(Key.Identifier)

  def toChain: Chain[Key.Identifier] = values.map(Key.Identifier)
}

object Identification {
  val Empty: Identification = Identification(Chain.empty)

  object / {
    def unapply[T](identification: Identification): Option[(Key.Identifier, Identification)] =
      identification.values.uncons.map(_.bimap(Key.Identifier, apply))
  }

  implicit val show: Show[Identification] = _.values.mkString_(" / ")
}
