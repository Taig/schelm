package io.taig.schelm.css.data

import io.taig.schelm.data.HtmlReference

final case class StyledHtmlReference[F[_]](styles: Map[Selector, Style], reference: HtmlReference[F])
