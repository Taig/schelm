package io.taig.schelm.css.data

import io.taig.schelm.data.Html

final case class StyledHtml[F[_]](styles: Map[Selector, Style], html: Html[F])
