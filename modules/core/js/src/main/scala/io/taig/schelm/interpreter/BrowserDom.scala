package io.taig.schelm.interpreter

import scala.scalajs.js

import cats.effect.implicits._
import cats.effect.{Effect, IO}
import cats.implicits._
import io.taig.schelm.algebra.Dom
import org.scalajs.dom
import org.scalajs.dom.Event

final class BrowserDom[F[_]](implicit F: Effect[F]) extends Dom[F] {
  override def unsafeRun(f: Any => F[Unit]): js.Function1[Event, _] =
    event =>
      f(event)
        .runAsync {
          case Right(_) => IO.unit
          case Left(throwable) =>
            IO {
              System.err.println("Failed to run event handler")
              throwable.printStackTrace(System.err)
            }
        }
        .unsafeRunSync()

  override def addEventListener(node: dom.Node, name: String, listener: js.Function1[Event, _]): F[Unit] =
    F.delay(node.addEventListener(name, listener))

  override def appendChild(parent: dom.Element, child: dom.Node): F[Unit] = F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[dom.Element] = document.createElement(name).pure[F]

  override def createElementNS(namespace: String, name: String): F[dom.Element] =
    document.createElementNS(namespace, name).pure[F]

  override def createTextNode(value: String): F[dom.Text] = document.createTextNode(value).pure[F]

  override def childAt(element: dom.Element, index: Int): F[Option[dom.Node]] =
    F.delay(Option(element.childNodes.apply(index)))

  override def children(element: dom.Element): F[List[dom.Node]] = F.delay {
    val result = collection.mutable.ListBuffer.empty[dom.Node]

    val childNodes = element.childNodes

    (0 until childNodes.length).foreach(index => result += childNodes.apply(index))

    result.toList
  }

  override def data(text: dom.Text, value: String): F[Unit] = F.delay(text.data = value)

  override def document: dom.Document = dom.document

  override def insertBefore(parent: dom.Element, node: dom.Node, reference: Option[dom.Node]): F[Unit] = {
    F.delay(parent.insertBefore(node, reference.orNull)).void
  }

  override def getAttribute(element: dom.Element, key: String): F[Option[String]] =
    F.delay(Option(element.getAttribute(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[dom.Element]] = F.delay(Option(document.getElementById(id)))

  override val head: F[Option[dom.Element]] = F.delay(Option(dom.document.head))

  override def innerHtml(element: dom.Element, value: String): F[Unit] = F.delay(element.innerHTML = value)

  override def parentNode(node: dom.Node): F[Option[dom.Node]] = F.delay(Option(node.parentNode))

  override def removeAttribute(element: dom.Element, key: String): F[Unit] = F.delay(element.removeAttribute(key))

  override def removeChild(parent: dom.Element, child: dom.Node): F[Unit] = F.delay(parent.removeChild(child)).void

  override def removeEventListener(node: dom.Node, name: String, listener: js.Function1[Event, _]): F[Unit] =
    F.delay(node.removeEventListener(name, listener))

  override def replaceChild(parent: dom.Element, current: dom.Node, next: dom.Node): F[Unit] =
    F.delay(parent.replaceChild(next, current)).void

  override def setAttribute(element: dom.Element, key: String, value: String): F[Unit] =
    F.delay(element.setAttribute(key, value))
}

object BrowserDom {
  def apply[F[_]: Effect]: BrowserDom[F] = new BrowserDom[F]
}
