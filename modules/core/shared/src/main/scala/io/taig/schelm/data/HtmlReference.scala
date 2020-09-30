package io.taig.schelm.data

import scala.annotation.tailrec

import io.taig.schelm.algebra.Dom
import cats.implicits._

final case class HtmlReference[F[_]](reference: NodeReference[F, HtmlReference[F]]) extends AnyVal {
  @tailrec
  def node: Node[F, ListenerReferences[F], HtmlReference[F]] = reference match {
    case NodeReference.Element(node, _)          => node
    case NodeReference.Fragment(node)            => node
    case NodeReference.Stateful(_, reference) => reference.node
    case NodeReference.Text(node, _)             => node
  }

  def html: Html[F] = Html(node.bimap(_.toListeners, _.html))

  def dom: Vector[Dom.Node] = reference match {
    case NodeReference.Element(_, dom)           => Vector(dom)
    case NodeReference.Fragment(node)            => node.children.indexed.flatMap(_.dom)
    case NodeReference.Stateful(_, reference) => reference.dom
    case NodeReference.Text(_, dom)              => Vector(dom)
  }
}
