package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.Dom
import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}
import scala.jdk.CollectionConverters._

import io.taig.schelm.data.Listener

final class JsoupDom[F[_]](val document: JDocument)(implicit F: Sync[F]) extends Dom[F] {
  override def unsafeRun(action: Listener.Action[F]): Unit = ()

  override def addEventListener(node: JNode, name: String, listener: Unit): F[Unit] = F.unit

  override def appendChild(parent: JElement, child: JNode): F[Unit] = F.delay(parent.appendChild(child)).void

  override def createElement(name: String): F[JElement] = document.createElement(name).pure[F]

  override def createElementNS(namespace: String, name: String): F[JElement] = createElement(name)

  override def createTextNode(value: String): F[JText] = new JText(value).pure[F]

  override def childAt(element: JElement, index: Int): F[Option[JNode]] =
    F.delay(element.child(index).some).recover { case _: IndexOutOfBoundsException => None }.widen

  override def children(element: JElement): F[List[JNode]] = F.delay(element.childNodes().asScala.toList)

  override def data(text: JText, value: String): F[Unit] = F.delay(text.text(value)).void

  override def insertBefore(parent: JElement, node: JNode, reference: Option[JNode]): F[Unit] =
    F.delay(reference.fold[JNode](parent.appendChild(node))(reference => reference.before(node))).void

  override def getAttribute(element: JElement, key: String): F[Option[String]] =
    F.delay(Some(element.attr(key)).filter(_.nonEmpty))

  override def getElementById(id: String): F[Option[JElement]] = F.delay(Option(document.getElementById(id)))

  override val head: F[Option[JElement]] = F.delay(Option(document.head))

  override def innerHtml(element: JElement, value: String): F[Unit] = F.delay(element.wrap(value)).void

  override def parentNode(node: JNode): F[Option[JNode]] = F.delay(Option(node.parentNode()))

  override def removeAttribute(element: JElement, key: String): F[Unit] = F.delay(element.removeAttr(key)).void

  override def removeChild(parent: JElement, child: JNode): F[Unit] = F.delay(child.remove())

  override def removeEventListener(node: JNode, name: String, listener: Unit): F[Unit] = F.unit

  override def replaceChild(parent: JElement, current: JNode, next: JNode): F[Unit] = F.delay(current.replaceWith(next))

  override def setAttribute(element: JElement, key: String, value: String): F[Unit] =
    F.delay(element.attr(key, value)).void
}

object JsoupDom {
  def apply[F[_]: Sync](document: JDocument): JsoupDom[F] = new JsoupDom[F](document)

  def default[F[_]: Sync]: JsoupDom[F] = JsoupDom(new JDocument("/"))
}
