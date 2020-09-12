package io.taig.schelm.interpreter

import cats.implicits._
import io.taig.schelm.algebra.Dom
import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}

import scala.jdk.CollectionConverters._

final class JsoupDom(val document: JDocument) extends Dom {
  override type Node = JNode

  override type Element = JElement

  override type Text = JText

  override type Document = JDocument

  override type Listener = Unit

  override def addEventListener(node: JNode, event: String, notify: Unit): Unit = ()

  override def appendChild(parent: JElement, child: JNode): Unit = parent.appendChild(child)

  override def createElement(name: String): JElement = new JElement(name)

  override def createElementNS(namespace: String, name: String): JElement = createElement(name)

  override def createTextNode(value: String): JText = new JText(value)

  override def data(text: JText, value: String): Unit = text.text(value)

  override def childAt(element: JElement, index: Int): Option[JNode] =
    try { element.child(index).some } catch { case _: IndexOutOfBoundsException => None }

  override def children(element: JElement): List[JNode] = element.childNodes().asScala.toList

  override def getAttribute(element: JElement, key: String): Option[String] =
    Some(element.attr(key)).filter(_.nonEmpty)

  override def getElementById(id: String): Option[JElement] = Option(document.getElementById(id))

  override val head: JElement = document.head

  override def innerHtml(element: JElement, value: String): Unit = element.wrap(value)

  override def insertBefore(parent: JElement, node: JNode, reference: Option[JNode]): Unit =
    reference.fold[JNode](parent.appendChild(node))(reference => reference.before(node))

  override def parentNode(node: JNode): Option[JNode] = Option(node.parentNode())

  override def removeChild(parent: JElement, child: JNode): Unit = child.remove()

  override def removeAttribute(element: JElement, key: String): Unit = element.removeAttr(key)

  override def removeEventListener(node: JNode, name: String, listener: Listener): Unit = ()

  override def replaceChild(parent: JElement, current: JNode, next: JNode): Unit =
    current.replaceWith(next)

  override def setAttribute(element: JElement, key: String, value: String): Unit =
    element.attr(key, value)
}

object JsoupDom {
  def apply(document: JDocument): Dom = new JsoupDom(document)

  def default: Dom = JsoupDom(new JDocument("/"))
}
