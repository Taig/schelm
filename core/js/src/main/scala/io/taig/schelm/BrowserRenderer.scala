package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

final class BrowserRenderer[F[_], A](
    registry: ListenerRegistry[F],
    send: A => Unit
)(implicit F: Sync[F])
    extends Renderer[F, A, Node[A, dom.Node]] {
  override def render(html: Html[A]): F[Node[A, dom.Node]] =
    render(html, Path.Empty)

  def render(html: Html[A], path: Path): F[Node[A, dom.Node]] = ???
//    html.value match {
//      case Component.Fragment(children) =>
//        children
//          .traverse((key, html) => render(html, path / segment(key)))
//          .map(children => Node(Component.Fragment(children), None))
//      case Component.Element(name, attributes, children) =>
//        for {
//          element <- Dom.createElement[F](name)
//          _ <- attributes.traverse_(register(element, path, _))
//          children <- children.traverse { (key, html) =>
//            render(html, path / segment(key))
//          }
//          _ <- Dom.appendAll(element, children.values.flatMap(_.head.toList))
//        } yield {
//          val component = Component.Element(name, attributes, children)
//          Node(component, element.some)
//        }
//      case component @ Component.Text(value) =>
//        Dom.createTextNode[F](value).map(node => Node(component, node.some))
//    }

  def segment(key: Key): String = s"[$key]"

  def register(
      element: dom.Element,
      path: Path,
      attribute: Attribute[A]
  ): F[Unit] =
    attribute.property match {
      case value: Value       => register(element, attribute.key, value)
      case event: Listener[A] => register(element, path, attribute.key, event)
    }

  def register(element: dom.Element, key: String, property: Value): F[Unit] =
    property.render.fold(F.unit)(Dom.setAttribute(element, key, _))

  def register(
      node: dom.Node,
      path: Path,
      key: String,
      event: Listener[A]
  ): F[Unit] = {
    val function = create(event)
    registry.register(key, path, function) *>
      Dom.addEventListener(node, key, function)
  }

  private def create(event: Listener[A]): js.Function1[dom.Event, _] =
    event match {
      case Listener.Pure(event) =>
        e =>
          e.preventDefault()
          send(event)
      case Listener.Input(event) =>
        e =>
          val value = e.target.asInstanceOf[HTMLInputElement].value
          send(event(value))
    }
}

object BrowserRenderer {
  def apply[F[_]: Sync, A](
      listener: ListenerRegistry[F],
      send: A => Unit
  ): BrowserRenderer[F, A] = new BrowserRenderer[F, A](listener, send)
}
