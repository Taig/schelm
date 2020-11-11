package io.taig.schelm.data

import cats.data.Chain

final case class Identification(values: Chain[Identifier]) extends AnyVal {
  def /(identifier: Identifier): Identification = Identification(values.append(identifier))
}

object Identification {
  val Empty: Identification = Identification(Chain.empty)
}
