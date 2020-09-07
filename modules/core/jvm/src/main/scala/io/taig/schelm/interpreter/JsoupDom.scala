package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.Dom
import io.taig.schelm.data.Listener.Action
import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}

import scala.jdk.CollectionConverters._

final class JsoupDom[F[_], Event](document: JDocument)(implicit F: Sync[F]) extends Dom[F, Event] {
  override type Node = JNode
  override type Element = JElement
  override type Text = JText
  override type Listener = Unit

  override def element(node: JNode): Option[JElement] = node match {
    case element: JElement => Some(element)
    case _                 => None
  }

  override def text(node: JNode): Option[JText] = node match {
    case text: JText => Some(text)
    case _           => None
  }

  override def callback(action: Action[Event]): Unit = ()

  override def addEventListener(node: JNode, event: String, notify: Unit): F[Unit] = F.unit

  override def appendChild(parent: JElement, child: JNode): F[Unit] = F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[JElement] = F.delay(new JElement(name))

  override def createElementNS(namespace: String, name: String): F[Element] = createElement(name)

  override def createTextNode(value: String): F[JText] = F.delay(new JText(value))

  override def data(text: JText, value: String): F[Unit] = F.delay(text.text(value)).void

  override def childAt(element: JElement, index: Int): F[Option[JNode]] =
    F.delay(element.child(index).some).recover { case _: IndexOutOfBoundsException => None }.widen

  override def children(element: JElement): F[List[JNode]] = F.delay(element.childNodes().asScala.toList)

  override def getAttribute(element: JElement, key: String): F[Option[String]] =
    F.delay(Some(element.attr(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[JElement]] = F.delay(Option(document.getElementById(id)))

  override val head: F[JElement] = F.delay(document.head)

  override def innerHtml(element: JElement, value: String): F[Unit] = F.delay(element.wrap(value)).void

  override def insertBefore(parent: JElement, node: JNode, reference: Option[Node]): F[Unit] =
    F.delay(reference.fold[Node](parent.appendChild(node))(reference => reference.before(node))).void

  override def parentNode(node: JNode): F[Option[Node]] = F.delay(Option(node.parentNode()))

  override def removeChild(parent: JElement, child: JNode): F[Unit] = F.delay(child.remove())

  override def removeAttribute(element: JElement, key: String): F[Unit] = F.delay(element.removeAttr(key)).void

  override def removeEventListener(node: JNode, name: String, listener: Unit): F[Unit] = F.unit

  override def replaceChild(parent: JElement, current: JNode, next: JNode): F[Unit] = F.delay(current.replaceWith(next))

  override def setAttribute(element: JElement, key: String, value: String): F[Unit] =
    F.delay(element.attr(key, value)).void
}

object JsoupDom {
  def apply[F[_]: Sync, Event](document: JDocument): Dom.Aux[F, Event, JNode, JElement, JText] =
    new JsoupDom[F, Event](document)

  def default[F[_]: Sync, Event]: Dom.Aux[F, Event, JNode, JElement, JText] = JsoupDom[F, Event](new JDocument("/"))
}
