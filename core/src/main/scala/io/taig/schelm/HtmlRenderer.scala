package io.taig.schelm

import cats.Monad
import cats.implicits._

final class HtmlRenderer[F[_], Event](dom: Dom[F, Event])(
    implicit F: Monad[F]
) extends Renderer[F, Html[Event], Reference[Event]] {
  override def render(
      html: Html[Event],
      path: Path
  ): F[Reference[Event]] =
    html.value match {
      case component: Component.Element[Html[Event], Event] =>
        for {
          element <- dom.createElement(component.name)
          _ <- component.attributes.traverse_(register(element, _))
          _ <- component.listeners.traverse_(register(element, path, _))
          children <- component.children.traverse(
            (key, child) => render(child, path / key)
          )
          _ <- dom.appendChildren(element, children.values.flatMap(_.root))
        } yield Reference.Element(component.copy(children = children), element)
      case component: Component.Fragment[Html[Event]] =>
        component.children
          .traverse((key, child) => render(child, path / key))
          .map(component.copy)
          .map(Reference.Fragment.apply)
      case component: Component.Lazy[Html[Event]] =>
        render(component.eval.value, path)
      case component: Component.Text =>
        dom
          .createTextNode(component.value)
          .map(Reference.Text(component, _))
    }

  def register(element: Element, attribute: Attribute): F[Unit] =
    attribute.value match {
      case Value.Flag(true)  => dom.setAttribute(element, attribute.key, "")
      case Value.Flag(false) => F.unit
      case Value.Multiple(values, accumulator) =>
        dom.setAttribute(
          element,
          attribute.key,
          values.mkString(accumulator.value)
        )
      case Value.One(value) => dom.setAttribute(element, attribute.key, value)
    }

  def register(
      element: Element,
      path: Path,
      listener: Listener[Event]
  ): F[Unit] =
    dom.addEventListener(
      element,
      listener.event,
      dom.lift(listener.action),
      path
    )
}

object HtmlRenderer {
  def apply[F[_]: Monad, Event](
      dom: Dom[F, Event]
  ): Renderer[F, Html[Event], Reference[Event]] =
    new HtmlRenderer[F, Event](dom)
}
