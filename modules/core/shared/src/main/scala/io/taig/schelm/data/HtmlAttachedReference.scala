package io.taig.schelm.data

import cats.effect.IO

final case class HtmlAttachedReference[+Event](
    reference: NodeReference[Event, HtmlAttachedReference[Event]],
    release: IO[Unit]
)
