package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import io.taig.schelm.util.PathTraversal

final case class HtmlReference[F[_]](reference: NodeReference[F, Listeners[F], HtmlReference[F]]) extends AnyVal {
//  def html: Html[F] = Html(reference.node.bimap(_.toListeners, _.html))

  def dom: Vector[Dom.Node] = reference match {
    case NodeReference.Element(_, dom) => Vector(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.dom)
    case NodeReference.Text(_, dom)    => Vector(dom)
  }
}

object HtmlReference {
//  implicit def traversal[F[_]]: PathTraversal[HtmlReference[F]] =
//    PathTraversal.ofNode[F, Listeners[F], HtmlReference[F]](
//      _.reference.node,
//      (html, reference) => html.copy(reference = html.reference)
//    )
}
