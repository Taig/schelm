package io.taig.schelm.css

import io.taig.schelm.Reference

case class StyledReference[Event](
    reference: Reference[Event],
    stylesheet: Stylesheet
)
