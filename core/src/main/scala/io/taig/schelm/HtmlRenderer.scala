package io.taig.schelm

import cats.Monad
import cats.implicits._

final class HtmlRenderer[F[_], Event, Node](dom: Dom[F, Event, Node])(
    implicit F: Monad[F]
) extends Renderer[F, Html[Event], Reference[Event, Node]] {
  override def render(html: Html[Event]): F[Reference[Event, Node]] =
    render(html, Path.Empty)

  def render(
      html: Html[Event],
      path: Path
  ): F[Reference[Event, Node]] =
    html.value match {
      case component: Component.Element[Html[Event], Event] =>
        for {
          element <- dom.createElement(component.name)
          _ <- component.attributes.traverse_(register(element, path, _))
          children <- component.children.traverse { (key, html) =>
            render(html, path / segment(key))
          }
          _ <- dom.appendChildren(element, children.values.flatMap(_.root))
        } yield Reference(component.copy(children = children), element.some)
      case component: Component.Fragment[Html[Event]] =>
        component.children
          .traverse((key, html) => render(html, path / segment(key)))
          .map(children => Reference(Component.Fragment(children), None))
      case component: Component.Lazy[Html[Event]] =>
        render(component.component.value, path)
      case component: Component.Text =>
        dom
          .createTextNode(component.value)
          .map(node => Reference(component, node.some))
    }

  def register(
      element: dom.Element,
      path: Path,
      attribute: Attribute[Event]
  ): F[Unit] = attribute match {
    case Attribute(key, value: Value) => register(element, key, value)
    case Attribute(key, listener: Listener[Event]) =>
      register(element, path, key, listener)
  }

  def register(element: dom.Element, key: String, value: Value): F[Unit] =
    value match {
      case Value.Flag(true)  => dom.setAttribute(element, key, "")
      case Value.Flag(false) => F.unit
      case Value.Multiple(values, accumulator) =>
        dom.setAttribute(element, key, values.mkString(accumulator.value))
      case Value.One(value) => dom.setAttribute(element, key, value)
    }

  def register(
      element: dom.Element,
      path: Path,
      key: String,
      listener: Listener[Event]
  ): F[Unit] =
    dom.addEventListener(element, key, path, dom.lift(listener))

  def segment(key: Key): String = s"[$key]"
}

object HtmlRenderer {
  def apply[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): Renderer[F, Html[Event], Reference[Event, Node]] =
    new HtmlRenderer[F, Event, Node](dom)
}
