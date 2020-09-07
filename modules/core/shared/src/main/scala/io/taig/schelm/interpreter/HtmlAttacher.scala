package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

/** Attach a `List` of `Node`s to a parent `Element`  */
final class HtmlAttacher[F[_]: Applicative, Node, Element <: Node](
    dom: Dom.Aux[F, _, Node, Element, _],
    parent: Element
) extends Attacher[F, List[Node], Element] {
  override def attach(nodes: List[Node]): F[Element] = nodes.traverse_(dom.appendChild(parent, _)).as(parent)
}

object HtmlAttacher {
  def apply[F[_]: Applicative, Node, Element <: Node](
      dom: Dom.Aux[F, _, Node, Element, _],
      parent: Element
  ): Attacher[F, List[Node], Element] = new HtmlAttacher(dom, parent)
}
