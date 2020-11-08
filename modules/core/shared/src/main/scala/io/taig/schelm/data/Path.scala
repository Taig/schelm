package io.taig.schelm.data

import cats.Show
import cats.data.Chain
import cats.implicits._

final case class Path(values: Chain[Key]) extends AnyVal {
  def /(key: Key): Path = Path(values.append(key))
}

object Path {
  val Root: Path = Path(Chain.empty)

  object / {
    def unapply[T](path: Path): Option[(Key, Path)] = path.values.uncons.map(_.map(apply))
  }

  implicit val show: Show[Path] = _.values.mkString_(" / ")
}
