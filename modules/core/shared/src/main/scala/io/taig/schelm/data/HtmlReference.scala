package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

final case class HtmlReference[F[_]](reference: ComponentReference[F, HtmlReference[F]]) extends AnyVal {
  def toNodes: List[Dom.Node] = reference match {
    case ComponentReference.Element(_, dom)     => List(dom)
    case ComponentReference.Fragment(component) => component.children.indexed.flatMap(_.toNodes)
    case ComponentReference.Text(_, dom)        => List(dom)
  }
}
