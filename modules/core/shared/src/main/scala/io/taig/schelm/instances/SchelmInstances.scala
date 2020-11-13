package io.taig.schelm.instances

import io.taig.schelm.algebra.Dom
import io.taig.schelm.data.{Attributes, Html, HtmlReference, NodeReference}
import io.taig.schelm.util.{NodeFunctor, NodeReferenceTraverse}

trait SchelmInstances {
  def nodeFunctorHtml[F[_]]: NodeFunctor[Html[F]] = new NodeFunctor[Html[F]] {
    override def mapAttributes(fa: Html[F])(f: Attributes => Attributes): Html[F] = ???
  }

  implicit def nodeReferenceTraverseHtmlReference[F[_]]: NodeReferenceTraverse[HtmlReference[F]] =
    new NodeReferenceTraverse[HtmlReference[F]] {
      override def dom(html: HtmlReference[F]): Vector[Dom.Node] = html.unfix match {
        case reference: NodeReference.Element[F, HtmlReference[F]] => Vector(reference.dom)
        case reference: NodeReference.Fragment[HtmlReference[F]]   => reference.node.children.values.flatMap(dom)
        case reference: NodeReference.Text[F]                      => Vector(reference.dom)
      }

      override def mapAttributes(fa: HtmlReference[F])(f: Attributes => Attributes): HtmlReference[F] = ???
    }
}

object SchelmInstances extends SchelmInstances
