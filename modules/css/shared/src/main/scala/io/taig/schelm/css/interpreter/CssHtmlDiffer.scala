package io.taig.schelm.css.interpreter

import cats.data.NonEmptyList
import io.taig.schelm.algebra.Differ
import io.taig.schelm.css.data._
import io.taig.schelm.data.Html
import io.taig.schelm.interpreter.HtmlDiffer
import io.taig.schelm.data.HtmlDiff

object CssHtmlDiffer {
  def apply[Event](
      html: Differ[Html[Event], HtmlDiff[Event]],
      css: Differ[Map[Selector, Style], NonEmptyList[CssDiff]]
  ): Differ[CssHtml[Event], CssHtmlDiff[Event]] = new Differ[CssHtml[Event], CssHtmlDiff[Event]] {
    override def diff(current: CssHtml[Event], next: CssHtml[Event]): Option[CssHtmlDiff[Event]] = {
      val ((currentHtml, currentCss), (nextHtml, nextCss)) = (CssHtml.toHtml(current), CssHtml.toHtml(next))
      CssHtmlDiff.fromOptions(html.diff(currentHtml, nextHtml), css.diff(currentCss, nextCss))
    }
  }

  def default[Event]: Differ[CssHtml[Event], CssHtmlDiff[Event]] = CssHtmlDiffer(HtmlDiffer[Event], CssDiffer)
}
