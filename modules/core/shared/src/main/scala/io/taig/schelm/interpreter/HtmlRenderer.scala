package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.{Element, Fragment, Html, Text}

final class HtmlRenderer[F[_]: Monad, Event, Node](dom: Dom.Node[F, Event, Node])
    extends Renderer[F, Html[Event], Node] {
  override def render(html: Html[Event]): F[List[Node]] = html.node match {
    case node: Element.Normal[Html[Event], Event] =>
      for {
        parent <- dom.createElement(node.tag.name)
        _ <- node.children.traverse_(render(_).flatMap(_.traverse_(dom.appendChild(parent, _))))
      } yield List(parent)
    case node: Element.Void[Event]   => dom.createElement(node.tag.name).map(_ :: Nil)
    case node: Fragment[Html[Event]] => node.children.indexed.flatTraverse(render)
    case node: Text[Event]           => dom.createTextNode(node.value).map(_ :: Nil)
  }
}

object HtmlRenderer {
  def apply[F[_]: Monad, Event, Node](dom: Dom.Node[F, Event, Node]): Renderer[F, Html[Event], Node] =
    new HtmlRenderer[F, Event, Node](dom)
}
