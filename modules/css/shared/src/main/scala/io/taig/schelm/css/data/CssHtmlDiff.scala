package io.taig.schelm.css.data

import cats.data.{Ior, NonEmptyList}
import io.taig.schelm.data.HtmlDiff

final case class CssHtmlDiff[F[_]](value: Ior[HtmlDiff[F], NonEmptyList[CssDiff]]) extends AnyVal

object CssHtmlDiff {
  def fromOptions[F[_]](html: Option[HtmlDiff[F]], css: Option[NonEmptyList[CssDiff]]): Option[CssHtmlDiff[F]] =
    Ior.fromOptions(html, css).map(apply)
}
