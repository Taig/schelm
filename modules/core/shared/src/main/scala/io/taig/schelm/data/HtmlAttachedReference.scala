package io.taig.schelm.data

import cats.implicits._
import io.taig.schelm.util.PathTraversal

final case class HtmlAttachedReference[F[_]](reference: NodeReference[F, HtmlAttachedReference[F]], release: F[Unit]) {
  def html: Html[F] = reference.node.bimap(_.toListeners, reference => Fix(reference.html))
}

object HtmlAttachedReference {
  implicit def traversal[F[_]]: PathTraversal[HtmlAttachedReference[F]] =
    PathTraversal
      .ofReference[F, HtmlAttachedReference[F]](_.reference, (html, reference) => html.copy(reference = reference))
}
