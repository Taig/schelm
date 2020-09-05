package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Element.Type
import io.taig.schelm.data.{Node => _, _}

final class HtmlRenderer[F[_]: Monad, Event, Node](dom: Dom.Node[F, Event, Node])
    extends Renderer[F, Html[Event], List[Node]] {
  override def render(html: Html[Event]): F[List[Node]] = html.node match {
    case node: Element[Event, Html[Event]] =>
      val children = node.tpe match {
        case Type.Normal(children) => children
        case Type.Void             => Children.Empty
      }

      render(node.tag, children)
    case node: Fragment[Html[Event]] => node.children.indexed.flatTraverse(render)
    case node: Text[Event]           => dom.createTextNode(node.value).map(_ :: Nil)
  }

  def render(tag: Tag[Event], children: Children[Html[Event]]): F[List[Node]] =
    for {
      parent <- dom.createElement(tag.name)
      _ <- tag.attributes.values.traverse_ {
        case Attribute(key, value) => dom.setAttribute(parent, key.value, value.value)
      }
      _ <- tag.listeners.values.traverse_ {
        case Listener(name, action) => dom.addEventListener(parent, name.value, dom.callback(action))
      }
      _ <- children.traverse_(render(_).flatMap(_.traverse_(dom.appendChild(parent, _))))
    } yield List(parent)
}

object HtmlRenderer {
  def apply[F[_]: Monad, Event, Node](dom: Dom.Node[F, Event, Node]): Renderer[F, Html[Event], List[Node]] =
    new HtmlRenderer[F, Event, Node](dom)
}
