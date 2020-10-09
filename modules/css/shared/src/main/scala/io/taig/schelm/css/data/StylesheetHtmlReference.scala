package io.taig.schelm.css.data

import io.taig.schelm.data.HtmlReference

final case class StylesheetHtmlReference[F[_]](stylesheet: Stylesheet, reference: HtmlReference[F])
