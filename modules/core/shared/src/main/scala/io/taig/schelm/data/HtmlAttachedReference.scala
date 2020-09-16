package io.taig.schelm.data

import cats.effect.IO

final case class HtmlAttachedReference[+Event, Node, Element <: Node, Text <: Node](
    reference: NodeReference[Event, Element, Text, HtmlAttachedReference[Event, Node, Element, Text]],
    release: IO[Unit]
)
