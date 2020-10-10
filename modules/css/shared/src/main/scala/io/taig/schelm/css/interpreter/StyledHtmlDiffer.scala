package io.taig.schelm.css.interpreter

import cats.data.NonEmptyList
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Html, HtmlDiff}
import io.taig.schelm.interpreter.HtmlDiffer

object StyledHtmlDiffer {
  def apply[F[_]](
      html: Differ[Html[F], HtmlDiff[F]],
      css: Differ[Map[Selector, Style], NonEmptyList[CssDiff]]
  ): Differ[StyledHtml[F], CssHtmlDiff[F]] = new Differ[StyledHtml[F], CssHtmlDiff[F]] {
    override def diff(current: StyledHtml[F], next: StyledHtml[F]): Option[CssHtmlDiff[F]] =
      CssHtmlDiff.fromOptions(html.diff(current.html, next.html), css.diff(current.styles, next.styles))
  }

  def default[F[_]]: Differ[StyledHtml[F], CssHtmlDiff[F]] = StyledHtmlDiffer(HtmlDiffer[F], CssDiffer)
}
