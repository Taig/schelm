package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.effect.std.Dispatcher
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data.Listener
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.raw.{HTMLDocument, HTMLInputElement}
import scala.scalajs.js

import io.taig.schelm.algebra.Dom.Window

final class BrowserDom[F[_]](dispatcher: Dispatcher[F])(implicit F: Sync[F]) extends Dom[F] {
  private val Value = "value"

  override def unsafeRun(action: Listener.Action[F]): js.Function1[Event, _] = { event =>
    dispatcher.unsafeRunAndForget {
      action(event).onError { throwable =>
        F.delay {
          System.err.println("Failed to run event handler")
          throwable.printStackTrace(System.err)
        }
      }
    }
  }

  override def addEventListener(target: dom.EventTarget, name: String, listener: Dom.Listener): F[Unit] =
    F.delay(target.addEventListener(name, listener))

  override def appendChild(parent: dom.Element, child: dom.Node): F[Unit] = F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[dom.Element] = document.map(_.createElement(name))

  override def createElementNS(namespace: String, name: String): F[dom.Element] =
    document.map(_.createElementNS(namespace, name))

  override def createTextNode(value: String): F[dom.Text] = document.map(_.createTextNode(value))

  override def childAt(element: dom.Element, index: Int): F[Option[dom.Node]] =
    F.delay(Option(element.childNodes.apply(index)))

  override def children(element: dom.Element): F[List[dom.Node]] = F.delay {
    val result = collection.mutable.ListBuffer.empty[dom.Node]

    val childNodes = element.childNodes

    (0 until childNodes.length).foreach(index => result += childNodes.apply(index))

    result.toList
  }

  override def data(text: dom.Text, value: String): F[Unit] = F.delay(text.data = value)

  override val document: F[HTMLDocument] = F.delay(dom.document)

  override def insertBefore(parent: dom.Element, node: dom.Node, reference: Option[dom.Node]): F[Unit] = {
    F.delay(parent.insertBefore(node, reference.orNull)).void
  }

  override def getAttribute(element: dom.Element, key: String): F[Option[String]] =
    (element, key) match {
      case (element: HTMLInputElement, Value) => F.delay(Some(element.value).filter(_.nonEmpty))
      case (_, _)                             => F.delay(Option(element.getAttribute(key)).filter(_.nonEmpty))
    }

  override def getElementById(id: String): F[Option[dom.Element]] =
    document.map(document => Option(document.getElementById(id)))

  override val head: F[Option[dom.Element]] = document.map(document => Option(document.head))

  override def innerHtml(element: dom.Element, value: String): F[Unit] = F.delay(element.innerHTML = value)

  override def parentNode(node: dom.Node): F[Option[dom.Node]] = F.delay(Option(node.parentNode))

  override def removeAttribute(element: dom.Element, key: String): F[Unit] = F.delay(element.removeAttribute(key))

  override def removeChild(parent: dom.Element, child: dom.Node): F[Unit] = F.delay(parent.removeChild(child)).void

  override def removeEventListener(target: dom.EventTarget, name: String, listener: Dom.Listener): F[Unit] =
    F.delay(target.removeEventListener(name, listener))

  override def replaceChild(parent: dom.Element, current: dom.Node, next: dom.Node): F[Unit] =
    F.delay(parent.replaceChild(next, current)).void

  override def setAttribute(element: dom.Element, key: String, value: String): F[Unit] =
    (element, key) match {
      case (element: HTMLInputElement, Value) => F.delay(element.value = value)
      case (_, _)                             => F.delay(element.setAttribute(key, value))
    }

  override val window: F[dom.Window] = F.delay(dom.window)
}

object BrowserDom {
  def apply[F[_]: Sync](dispatcher: Dispatcher[F]): BrowserDom[F] = new BrowserDom[F](dispatcher)
}
