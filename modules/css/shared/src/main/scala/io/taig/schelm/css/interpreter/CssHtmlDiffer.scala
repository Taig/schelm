package io.taig.schelm.css.interpreter

import cats.data.NonEmptyList
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data._
import io.taig.schelm.data.{Html, HtmlDiff}
import io.taig.schelm.interpreter.HtmlDiffer

object CssHtmlDiffer {
  def apply(
      html: Differ[Html, HtmlDiff],
      css: Differ[Map[Selector, Style], NonEmptyList[CssDiff]]
  ): Differ[CssHtml, CssHtmlDiff] = new Differ[CssHtml, CssHtmlDiff] {
    override def diff(current: CssHtml, next: CssHtml): Option[CssHtmlDiff] = {
      val ((currentHtml, currentCss), (nextHtml, nextCss)) = (CssHtml.toHtml(current), CssHtml.toHtml(next))
      CssHtmlDiff.fromOptions(html.diff(currentHtml, nextHtml), css.diff(currentCss, nextCss))
    }
  }

  def default: Differ[CssHtml, CssHtmlDiff] = CssHtmlDiffer(HtmlDiffer, CssDiffer)
}
