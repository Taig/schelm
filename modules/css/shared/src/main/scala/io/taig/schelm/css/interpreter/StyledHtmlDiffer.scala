package io.taig.schelm.css.interpreter

import cats.Applicative
import cats.data.{Kleisli, NonEmptyList}
import cats.implicits._
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Html, HtmlDiff}
import io.taig.schelm.interpreter.HtmlDiffer

object StyledHtmlDiffer {
  def apply[F[_]: Applicative](
      html: Differ[F, Html[F], HtmlDiff[F]],
      css: Differ[F, Map[Selector, Style], NonEmptyList[CssDiff]]
  ): Differ[F, StyledHtml[F], CssHtmlDiff[F]] =
    Kleisli {
      case (current, next) =>
        (html.run((current.html, next.html)), css.run((current.styles, next.styles))).mapN(CssHtmlDiff.fromOptions)
    }

  def default[F[_]: Applicative]: Differ[F, StyledHtml[F], CssHtmlDiff[F]] =
    StyledHtmlDiffer(HtmlDiffer[F], CssDiffer[F])
}
