package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

/** Attach a `List` of `Node`s to a parent `Element`  */
final class HtmlAttacher[F[_]: Applicative](dom: Dom[F], parent: Dom.Element)
    extends Attacher[F, List[Dom.Node], Dom.Element] {
  override def attach(nodes: List[Dom.Node]): F[Dom.Element] = nodes.traverse_(dom.appendChild(parent, _)).as(parent)
}

object HtmlAttacher {
  def apply[F[_]: Applicative](dom: Dom[F], parent: Dom.Element): Attacher[F, List[Dom.Node], Dom.Element] =
    new HtmlAttacher(dom, parent)
}
