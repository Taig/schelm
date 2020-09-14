package io.taig.schelm.css.data

import cats.data.{Ior, NonEmptyList}
import io.taig.schelm.data.HtmlDiff

final case class CssHtmlDiff(value: Ior[HtmlDiff, NonEmptyList[CssDiff]]) extends AnyVal

object CssHtmlDiff {
  def fromOptions(html: Option[HtmlDiff], css: Option[NonEmptyList[CssDiff]]): Option[CssHtmlDiff] =
    Ior.fromOptions(html, css).map(apply)
}
