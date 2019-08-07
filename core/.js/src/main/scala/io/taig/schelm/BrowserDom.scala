package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

final class BrowserDom[F[_], Event](
    registry: ListenerRegistry[F],
    manager: EventManager[F, Event]
)(
    implicit F: Sync[F]
) extends Dom[F, Event] {
  override type Notify = js.Function1[dom.Event, _]

  override def lift(listener: Action[Event]): js.Function1[dom.Event, _] =
    listener match {
      case Action.Pure(event) =>
        e =>
          e.preventDefault()
          manager.submitUnsafe(event)
      case Action.Input(event) =>
        e =>
          val value = e.target.asInstanceOf[HTMLInputElement].value
          manager.submitUnsafe(event(value))
    }

  override def element(value: dom.Node): F[dom.Element] =
    value match {
      case element: dom.Element => element.pure[F]
      case _                    => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
    }

  override def text(value: dom.Node): F[dom.Text] =
    value match {
      case text: dom.Text => text.pure[F]
      case _              => EffectHelpers.fail[F]("Not a Text. Dom out of sync?")
    }

  override def addEventListener(
      node: dom.Node,
      event: String,
      listener: Notify,
      path: Path
  ): F[Unit] = {
    registry.register(event, path, listener) *>
      F.delay(node.addEventListener(event, listener))
  }

  override def appendChild(parent: dom.Element, child: dom.Node): F[Unit] =
    F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[dom.Element] =
    F.delay(document.createElement(name))

  override def createTextNode(value: String): F[dom.Text] =
    F.delay(document.createTextNode(value))

  override def data(text: dom.Text, value: String): F[Unit] =
    F.delay(text.data = value)

  override def childAt(element: dom.Element, index: Int): F[Option[Node]] =
    F.delay(Option(element.childNodes.apply(index)))

  override def children(element: dom.Element): F[List[Node]] =
    F.delay(element.childNodes).map { children =>
      (0 until children.length).foldLeft(List.empty[Node])(_ :+ children(_))
    }

  override def getAttribute(
      element: dom.Element,
      key: String
  ): F[Option[String]] =
    F.delay(Option(element.getAttribute(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[dom.Element]] =
    F.delay(Option(document.getElementById(id)))

  override def head: F[dom.Element] = F.delay(document.head)

  override def innerHtml(element: dom.Element, value: String): F[Unit] =
    F.delay(element.innerHTML = value)

  override def removeChild(parent: dom.Element, child: dom.Node): F[Unit] =
    F.delay(parent.removeChild(child)).void

  override def removeAttribute(element: dom.Element, key: String): F[Unit] =
    F.delay(element.removeAttribute(key))

  override def removeEventListener(
      node: dom.Node,
      name: String,
      path: Path
  ): F[Unit] =
    registry.unregister(name, path).flatMap { register =>
      F.delay(node.removeEventListener(name, register))
    }

  override def setAttribute(
      element: dom.Element,
      key: String,
      value: String
  ): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

object BrowserDom {
  def apply[F[_]: Sync, Event](
      manager: EventManager[F, Event]
  ): F[Dom[F, Event]] =
    ListenerRegistry[F].map(new BrowserDom[F, Event](_, manager))
}
