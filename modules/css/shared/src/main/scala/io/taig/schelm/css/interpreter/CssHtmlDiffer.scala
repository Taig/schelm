package io.taig.schelm.css.interpreter

import cats.data.NonEmptyList
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data._
import io.taig.schelm.data.Html
import io.taig.schelm.interpreter.HtmlDiffer
import io.taig.schelm.data.HtmlDiff

object CssHtmlDiffer {
  def apply[F[_]](
      html: Differ[Html[F], HtmlDiff[F]],
      css: Differ[Map[Selector, Style], NonEmptyList[CssDiff]]
  ): Differ[CssHtml[F], CssHtmlDiff[F]] = new Differ[CssHtml[F], CssHtmlDiff[F]] {
    override def diff(current: CssHtml[F], next: CssHtml[F]): Option[CssHtmlDiff[F]] = {
      val ((currentHtml, currentCss), (nextHtml, nextCss)) = (CssHtml.toHtml(current), CssHtml.toHtml(next))
      CssHtmlDiff.fromOptions(html.diff(currentHtml, nextHtml), css.diff(currentCss, nextCss))
    }
  }

  def default[F[_]]: Differ[CssHtml[F], CssHtmlDiff[F]] = CssHtmlDiffer(HtmlDiffer[F], CssDiffer)
}
