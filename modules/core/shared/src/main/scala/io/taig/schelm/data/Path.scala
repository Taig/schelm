package io.taig.schelm.data

import cats.Show
import cats.data.Chain
import cats.implicits._

final case class Path(identification: Identification, indices: Chain[Int]) {
  def /(key: Key): Path = key match {
    case identifier: Key.Identifier => Path(identification / Identifier(identifier.value), Path.EmptyIndices)
    case index: Key.Index           => Path(identification, indices.append(index.value))
  }
}

object Path {
  private val EmptyIndices: Chain[Int] = Chain.empty

  val Root: Path = Path(Identification.Root, EmptyIndices)

  implicit val show: Show[Path] = {
    case Path(identification, Chain.nil) => identification.show
    case Path(identification, indices)   => show"$identification / ${indices.mkString_(" / ")}"
  }
}
