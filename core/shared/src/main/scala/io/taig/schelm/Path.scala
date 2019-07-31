package io.taig.schelm

import cats.data.Chain
import cats.implicits._

final case class Path(values: Chain[String]) extends AnyVal {
  def /(segment: String): Path =
    if (segment.isEmpty) this else Path(values.append(segment))

  def /(index: Int): Path = this / s"[$index]"

  def render: String = values.mkString_(" / ")
}

object Path {
  val Empty: Path = Path(Chain.empty)
}