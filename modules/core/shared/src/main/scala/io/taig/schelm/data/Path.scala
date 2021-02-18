package io.taig.schelm.data

import cats.data.Chain

final case class Path(identification: Identification, indices: Chain[Int]) {
  def /(identifier: Identifier): Path = Path.fromIdentification(identification / identifier)
}

object Path {
  val Root: Path = Path(Identification.Root, Chain.empty)

  def fromIdentification(identification: Identification): Path = Root.copy(identification = identification)
}
