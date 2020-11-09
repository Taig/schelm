package io.taig.schelm.data

final case class HtmlHydratedReference[F[_]](
    reference: NodeReference[F, ListenerReferences, HtmlHydratedReference[F]],
    release: F[Unit]
)
