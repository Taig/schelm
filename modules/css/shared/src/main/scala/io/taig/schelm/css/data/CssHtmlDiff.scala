package io.taig.schelm.css.data

import cats.data.{Ior, NonEmptyList}
import io.taig.schelm.data.HtmlDiff

final case class CssHtmlDiff[Event](value: Ior[HtmlDiff[Event], NonEmptyList[CssDiff]]) extends AnyVal

object CssHtmlDiff {
  def fromOptions[Event](
      html: Option[HtmlDiff[Event]],
      css: Option[NonEmptyList[CssDiff]]
  ): Option[CssHtmlDiff[Event]] =
    Ior.fromOptions(html, css).map(apply)
}
