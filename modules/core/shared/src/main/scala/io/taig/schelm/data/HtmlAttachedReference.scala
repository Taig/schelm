package io.taig.schelm.data

import io.taig.schelm.util.PathTraversal

final case class HtmlAttachedReference[F[_]](reference: NodeReference[F, HtmlAttachedReference[F]], release: F[Unit])

object HtmlAttachedReference {
  implicit def traversal[F[_]]: PathTraversal[HtmlAttachedReference[F]] =
    PathTraversal
      .ofReference[F, HtmlAttachedReference[F]](_.reference, (html, reference) => html.copy(reference = reference))
}
