package io.taig.schelm.css

import io.taig.schelm.{Html, Reference}

final case class StyledReference[Event, Node](
    reference: Reference[Event, Node],
    styleheet: Stylesheet
)
