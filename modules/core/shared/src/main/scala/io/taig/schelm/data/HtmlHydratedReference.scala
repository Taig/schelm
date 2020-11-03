package io.taig.schelm.data

final case class HtmlHydratedReference[F[_]](
    reference: NodeReference[F, ListenerReferences[F], HtmlHydratedReference[F]],
    release: F[Unit]
)
