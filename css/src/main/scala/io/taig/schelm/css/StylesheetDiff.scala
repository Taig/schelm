package io.taig.schelm.css

import cats.data.NonEmptyList
import cats.implicits._

sealed abstract class StylesheetDiff extends Product with Serializable

object StylesheetDiff {
  final case class AddRule(rule: Rule) extends StylesheetDiff
  final case class RemoveRule(rule: Rule) extends StylesheetDiff
  final case class Group(diffs: NonEmptyList[StylesheetDiff])
      extends StylesheetDiff

  def from(diffs: Iterable[StylesheetDiff]): Option[StylesheetDiff] =
    diffs.toList match {
      case Nil          => None
      case head :: Nil  => head.some
      case head :: tail => Group(NonEmptyList(head, tail)).some
    }
}
