package io.taig.schelm.css

import cats.Monad
import cats.implicits._
import io.taig.schelm.css.internal.StyleHelpers
import io.taig.schelm.{Attacher, Dom, NodesAttacher}

final class StyledNodesAttacher[F[_]: Monad, Event, Node](
    dom: Dom[F, Event, Node],
    attacher: Attacher[F, Node, List[Node]]
) extends Attacher[F, Node, StyledNodes[Node]] {
  override def attach(
      node: Node,
      reference: StyledNodes[Node]
  ): F[Unit] = {
    for {
      _ <- attacher.attach(node, reference.nodes)
      style <- StyleHelpers.getOrCreateStyleElement(dom)
      css = reference.stylesheet.toString
      _ <- dom.innerHtml(style, css)
    } yield ()
  }
}

object StyledNodesAttacher {
  def apply[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): Attacher[F, Node, StyledNodes[Node]] =
    new StyledNodesAttacher[F, Event, Node](dom, NodesAttacher(dom))
}
