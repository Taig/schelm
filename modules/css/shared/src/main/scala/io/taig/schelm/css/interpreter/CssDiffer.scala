package io.taig.schelm.css.interpreter

import cats.Applicative
import cats.implicits._
import cats.data.{Kleisli, NonEmptyList}
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data.{CssDiff, Selector, Style}

object CssDiffer {
  def apply[F[_]: Applicative]: Differ[F, Map[Selector, Style], NonEmptyList[CssDiff]] = {
    def diff(current: Map[Selector, Style], next: Map[Selector, Style]): Option[NonEmptyList[CssDiff]] = {
      val left = current.keys.toList
      val right = next.keys.toList
      val removed = (left diff right).map(CssDiff.Remove)
      val added = (right diff left).map(selector => CssDiff.Add(selector, next(selector)))
      NonEmptyList.fromList(removed ++ added)
    }

    Kleisli { case (current, next) => diff(current, next).pure[F] }
  }
}
