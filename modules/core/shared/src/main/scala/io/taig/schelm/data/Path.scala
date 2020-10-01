package io.taig.schelm.data

import cats.data.Chain

final case class Path(values: Chain[Path.Segment]) extends AnyVal {
  def /(key: Key): Path = Path(values.append(Path.Segment.Child(key)))

  def /(segment: Path.Segment): Path = Path(values.append(segment))
}

object Path {
  sealed abstract class Segment extends Product with Serializable

  object Segment {
    final case class Child(key: Key) extends Segment
    final case object Stateful extends Segment
  }

  val Empty: Path = Path(Chain.empty)
}
