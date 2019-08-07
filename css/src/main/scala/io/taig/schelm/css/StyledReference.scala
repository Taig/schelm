package io.taig.schelm.css

import io.taig.schelm.Reference

case class StyledReference[A](
    reference: Reference[A],
    stylesheet: Stylesheet
)
