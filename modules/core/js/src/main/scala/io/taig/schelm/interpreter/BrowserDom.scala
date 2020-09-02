package io.taig.schelm.interpreter

import cats.effect.{Effect, IO, Sync}
import cats.effect.implicits._
import io.taig.schelm.algebra.{Dom, EventManager}
import io.taig.schelm.data.Listener
import org.scalajs.dom
import org.scalajs.dom.document
import cats.implicits._
import io.taig.schelm.data.Listener.Action
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

final class BrowserDom[F[_]: Effect, Event](events: EventManager[F, Event])(implicit F: Sync[F]) extends Dom[F, Event] {
  override type Element = dom.Element
  override type Node = dom.Node
  override type Text = dom.Text
  override type Listener = js.Function1[dom.Event, _]

  override def callback(listener: Listener.Action[Event]): js.Function1[dom.Event, _] =
    listener match {
      case Action.Pure(event) =>
        native =>
          native.preventDefault()
          unsafeSubmit(event)
      case Action.Input(event) =>
        native =>
          val value = native.target.asInstanceOf[HTMLInputElement].value
          unsafeSubmit(event(value))
    }

  def unsafeSubmit(event: Event): Unit =
    events
      .submit(event)
      .runAsync {
        case Right(_) => IO.unit
        case Left(throwable) =>
          IO {
            System.err.println("Failed to submit event")
            throwable.printStackTrace(System.err)
          }
      }
      .unsafeRunSync()

  override def addEventListener(node: Node, name: String, listener: js.Function1[dom.Event, _]): F[Unit] =
    F.delay(node.addEventListener(name, listener))

  override def appendChild(parent: Element, child: Node): F[Unit] = F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[Element] = F.delay(document.createElement(name))

  override def createElementNS(namespace: String, name: String): F[Element] =
    F.delay(document.createElementNS(namespace, name))

  override def createTextNode(value: String): F[Text] = F.delay(document.createTextNode(value))

  override def childAt(element: Element, index: Int): F[Option[Node]] = F.delay(Option(element.childNodes.apply(index)))

  override def children(element: Element): F[List[Node]] = F.delay {
    val result = collection.mutable.ListBuffer.empty[Node]

    val childNodes = element.childNodes

    (0 until childNodes.length).foreach { index => result.addOne(childNodes.apply(index)) }

    result.toList
  }

  override def data(text: Text, value: String): F[Unit] = F.delay(text.data = value)

  override def getAttribute(element: Element, key: String): F[Option[String]] =
    F.delay(Option(element.getAttribute(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[Element]] =
    F.delay(Option(document.getElementById(id)))

  override val head: F[Element] = F.delay(document.head)

  override def innerHtml(element: Element, value: String): F[Unit] = F.delay(element.innerHTML = value)

  override def removeAttribute(element: Element, key: String): F[Unit] = F.delay(element.removeAttribute(key))

  override def removeChild(parent: Element, child: Node): F[Unit] = F.delay(parent.removeChild(child)).void

  override def removeEventListener(node: Node, name: String, listener: js.Function1[dom.Event, _]): F[Unit] =
    F.delay(node.removeEventListener(name, listener))

  override def setAttribute(element: Element, key: String, value: String): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

object BrowserDom {
  def apply[F[_]: Effect, Event](events: EventManager[F, Event]): Dom.Aux[F, Event, dom.Node, dom.Element, dom.Text] =
    new BrowserDom[F, Event](events)
}
