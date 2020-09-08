package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Element.Type
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Monad, Event](dom: Dom[F, Event]) extends Renderer[F, Html[Event], List[Dom.Node]] {
  override def render(html: Html[Event]): F[List[Dom.Node]] = html.node match {
    case node: Element[Event, Html[Event]] =>
      val children = node.tpe match {
        case Type.Normal(children) => children
        case Type.Void             => Children.Empty
      }

      render(node.tag, children)
    case node: Fragment[Html[Event]] => node.children.indexed.flatTraverse(render)
    case node: Text[Event]           => dom.createTextNode(node.value).map(_ :: Nil)
  }

  def render(tag: Tag[Event], children: Children[Html[Event]]): F[List[Dom.Node]] =
    for {
      parent <- dom.createElement(tag.name)
      _ <- tag.attributes.toList.traverse_ {
        case Attribute(key, value) => dom.setAttribute(parent, key.value, value.value)
      }
      _ <- tag.listeners.toList.traverse_ {
        case Listener(name, action) => dom.addEventListener(parent, name.value, dom.callback(action))
      }
      _ <- children.traverse_(render(_).flatMap(_.traverse_(dom.appendChild(parent, _))))
    } yield List(parent)
}

object HtmlRenderer {
  def apply[F[_]: Monad, Event, Node](dom: Dom[F, Event]): Renderer[F, Html[Event], List[Dom.Node]] =
    new HtmlRenderer[F, Event](dom)
}
