package io.taig.schelm.data

final case class HtmlHydratedReference[F[_]](reference: NodeReference[F, HtmlHydratedReference[F]], release: F[Unit])
