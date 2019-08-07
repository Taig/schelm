package io.taig.schelm

import cats.data.Chain
import cats.implicits._

final case class Path(values: Chain[String]) extends AnyVal {
  def /(segment: String): Path =
    if (segment.isEmpty) this else Path(values.append(segment))

  def /(key: Key): Path = this / s"[$key]"

  override def toString: String = values.mkString_(" / ")
}

object Path {
  val Empty: Path = Path(Chain.empty)

  val Root: Path = Path(Chain.one("root"))
}
