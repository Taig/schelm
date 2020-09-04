package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

final class HtmlAttacher[F[_]: Applicative, Node, Element](dom: Dom.Aux[F, _, Node, Element, _])
    extends Attacher[F, List[Node], Element] {
  override def attach(parent: Element, nodes: List[Node]): F[Unit] = nodes.traverse_(dom.appendChild(parent, _))
}

object HtmlAttacher {
  def apply[F[_]: Applicative, Node, Element](dom: Dom.Aux[F, _, Node, Element, _]): Attacher[F, List[Node], Element] =
    new HtmlAttacher(dom)
}
