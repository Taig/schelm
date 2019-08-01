package io.taig.schelm

import cats.Monad
import cats.implicits._

final class DomRenderer[F[_], A, B](dom: Dom[F, A, B])(implicit F: Monad[F])
    extends Renderer[F, A, B] {
  override def render(html: Html[A], path: Path): F[Node[A, B]] =
    html.value match {
      case component: Component.Element[Html[A], A] =>
        for {
          element <- dom.createElement(component.name)
          _ <- component.attributes.traverse_(register(element, path, _))
          children <- component.children.traverse { (key, html) =>
            render(html, path / segment(key))
          }
          _ <- dom.appendChildren(element, children.values.flatMap(_.root))
        } yield Node(component.copy(children = children), element.some)
      case component: Component.Fragment[Html[A], A] =>
        component.children
          .traverse((key, html) => render(html, path / segment(key)))
          .map(children => Node(Component.Fragment(children), None))
      case component: Component.Text =>
        dom
          .createTextNode(component.value)
          .map(node => Node(component, node.some))
    }

  def register(
      element: dom.Element,
      path: Path,
      attribute: Attribute[A]
  ): F[Unit] = attribute match {
    case Attribute(key, value: Value) => register(element, key, value)
    case Attribute(key, listener: Listener[A]) =>
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
      listener: Listener[A]
  ): F[Unit] = {
    // TODO ListenerRegister
    val trigger = dom.lift(listener)
    dom.addEventListener(element, key, trigger)
  }

  def segment(key: Key): String = s"[$key]"
}
