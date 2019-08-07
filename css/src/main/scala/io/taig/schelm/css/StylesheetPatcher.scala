package io.taig.schelm.css

import cats.Applicative
import cats.implicits._
import io.taig.schelm._

final class StylesheetPatcher[F[_]: Applicative, A](dom: Dom[F, A]) {
  def patch(
      style: Element,
      stylesheet: Stylesheet,
      diff: StylesheetDiff
  ): F[Stylesheet] =
    adds(diff) match {
      case Nil => stylesheet.pure[F]
      case rules =>
        val update = rules.foldLeft(stylesheet)(_ + _)
        dom.innerHtml(style, update.toString) *> update.pure[F]
    }

  def adds(diff: StylesheetDiff): List[Rule] =
    diff match {
      case StylesheetDiff.AddRule(rule) => List(rule)
      case StylesheetDiff.RemoveRule(_) => List.empty
      case StylesheetDiff.Group(diffs) =>
        diffs.foldLeft(List.empty[Rule])((rules, diff) => rules ++ adds(diff))
    }
}

object StylesheetPatcher {
  def apply[F[_]: Applicative, A](dom: Dom[F, A]) =
    new StylesheetPatcher[F, A](dom)
}
