package io.taig.schelm.css

import io.taig.schelm.Html

final case class StyledHtml[A](html: Html[A], styleheet: Stylesheet)
