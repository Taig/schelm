package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

final class HtmlAttacher[F[_]: Applicative, Node, Element](dom: Dom.Aux[F, _, Node, Element, _], root: Element)
    extends Attacher[F, List[Node]] {
  override def attach(nodes: List[Node]): F[Unit] = nodes.traverse_(dom.appendChild(root, _))
}

object HtmlAttacher {
  def apply[F[_]: Applicative, Node, Element](
      dom: Dom.Aux[F, _, Node, Element, _],
      root: Element
  ): Attacher[F, List[Node]] = new HtmlAttacher(dom, root)
}
