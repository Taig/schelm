package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import cats.implicits._

final case class HtmlReference[F[_]](reference: NodeReference[F, HtmlReference[F]]) extends AnyVal {
  def node: Node[F, ListenerReferences[F], HtmlReference[F]] = reference match {
    case NodeReference.Element(node, _) => node
    case NodeReference.Fragment(node)   => node
    case NodeReference.Text(node, _)    => node
  }

  def html: Html[F] = Html(node.bimap(_.toListeners, _.html))

  def dom: List[Dom.Node] = reference match {
    case NodeReference.Element(_, dom) => List(dom)
    case NodeReference.Fragment(node)  => node.children.indexed.flatMap(_.dom)
    case NodeReference.Text(_, dom)    => List(dom)
  }
}
