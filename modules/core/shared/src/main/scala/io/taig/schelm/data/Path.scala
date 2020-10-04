package io.taig.schelm.data

import cats.data.Chain

final case class Path(values: Chain[Key]) extends AnyVal {
  def /(key: Key): Path = Path(values.append(key))
}

object Path {
  val Empty: Path = Path(Chain.empty)
}
