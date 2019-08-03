package io.taig.schelm.css

import io.taig.schelm.Reference

final case class StyledReference[Event, Node](
    reference: Reference[Event, Node],
    stylesheet: Stylesheet
)
