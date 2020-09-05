package io.taig.schelm.css.interpreter

import cats.data.NonEmptyList
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data.{CssDiff, Selector, Style}

object CssDiffer extends Differ[Map[Selector, Style], NonEmptyList[CssDiff]] {
  override def diff(current: Map[Selector, Style], next: Map[Selector, Style]): Option[NonEmptyList[CssDiff]] = {
    val left = current.keys.toList
    val right = next.keys.toList
    val removed = (left diff right).map(CssDiff.Remove)
    val added = (right diff left).map(selector => CssDiff.Add(selector, next(selector)))
    NonEmptyList.fromList(removed ++ added)
  }
}
