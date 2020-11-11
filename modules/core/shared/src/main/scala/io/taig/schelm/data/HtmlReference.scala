package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import io.taig.schelm.util.NodeReferenceTraverse

final case class HtmlReference[F[_]](value: NodeReference[F, HtmlReference[F]]) extends AnyVal

object HtmlReference {
  implicit def traverse[F[_]]: NodeReferenceTraverse[HtmlReference[F]] = new NodeReferenceTraverse[HtmlReference[F]] {
    override def dom(html: HtmlReference[F]): Vector[Dom.Node] = html.value match {
      case reference: NodeReference.Element[F, HtmlReference[F]] => Vector(reference.dom)
      case reference: NodeReference.Fragment[HtmlReference[F]]   => reference.node.children.values.flatMap(dom)
      case reference: NodeReference.Text[F]                      => Vector(reference.dom)
    }

    override def mapAttributes(fa: HtmlReference[F])(f: Attributes => Attributes): HtmlReference[F] = ???
  }
}
