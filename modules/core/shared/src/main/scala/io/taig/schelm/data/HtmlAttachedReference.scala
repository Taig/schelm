package io.taig.schelm.data

final case class HtmlAttachedReference[F[_]](reference: NodeReference[F, HtmlAttachedReference[F]], release: F[Unit])
