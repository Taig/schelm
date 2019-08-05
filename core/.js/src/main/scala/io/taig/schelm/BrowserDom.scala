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
) extends Dom[F, Event, dom.Node] {
  override type Element = dom.Element
  override type Text = dom.Text
  override type Notify = js.Function1[dom.Event, _]

  override def lift(listener: Listener[Event]): js.Function1[dom.Event, _] =
    listener match {
      case Listener.Pure(event) =>
        e =>
          e.preventDefault()
          manager.submitUnsafe(event)
      case Listener.Input(event) =>
        e =>
          val value = e.target.asInstanceOf[HTMLInputElement].value
          manager.submitUnsafe(event(value))
    }

  override def element(value: dom.Node): F[Element] =
    value match {
      case element: Element => element.pure[F]
      case _                => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
    }

  override def text(value: dom.Node): F[Text] =
    value match {
      case text: Text => text.pure[F]
      case _          => EffectHelpers.fail[F]("Not a Text. Dom out of sync?")
    }

  override def addEventListener(
      node: dom.Node,
      name: String,
      path: Path,
      listener: Notify
  ): F[Unit] = {
    registry.register(name, path, listener) *>
      F.delay(node.addEventListener(name, listener))
  }

  override def appendChild(parent: dom.Element, child: dom.Node): F[Unit] =
    F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[Element] =
    F.delay(document.createElement(name))

  override def createTextNode(value: String): F[Text] =
    F.delay(document.createTextNode(value))

  override def data(text: Text, value: String): F[Unit] =
    F.delay(text.data = value)

  override def getAttribute(element: Element, key: String): F[Option[String]] =
    F.delay(Option(element.getAttribute(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[Element]] =
    F.delay(Option(document.getElementById(id)))

  override def head: F[Element] = F.delay(document.head)

  override def innerHtml(element: Element, value: String): F[Unit] =
    F.delay(element.innerHTML = value)

  override def removeChild(parent: Element, child: dom.Node): F[Unit] =
    F.delay(parent.removeChild(child)).void

  override def removeAttribute(element: Element, key: String): F[Unit] =
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
      element: Element,
      key: String,
      value: String
  ): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

object BrowserDom {
  def apply[F[_]: Sync, Event](
      manager: EventManager[F, Event]
  ): F[Dom[F, Event, dom.Node]] =
    ListenerRegistry[F].map(new BrowserDom[F, Event](_, manager))
}
