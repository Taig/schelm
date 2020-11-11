package io.taig.schelm.css.data

import io.taig.schelm.data.HtmlHydratedReference

final case class StyledHtmlHydratedReference[F[_]](styles: Map[Selector, Style], html: HtmlHydratedReference[F])
