package io.taig.schelm.css.data

import io.taig.schelm.data.HtmlAttachedReference

final case class StylesheetHtmlAttachedReference[F[_]](stylesheet: Stylesheet, reference: HtmlAttachedReference[F])
