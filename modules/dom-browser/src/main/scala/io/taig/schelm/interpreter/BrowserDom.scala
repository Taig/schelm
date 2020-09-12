package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.Dom
import org.scalajs.dom
import org.scalajs.dom.{html, Event}

import scala.scalajs.js

object BrowserDom extends Dom {
  override type Node = dom.Node

  override type Element = dom.Element

  override type Text = dom.Text

  override type Document = html.Document

  override type Listener = js.Function1[dom.Event, _]

  override def addEventListener(node: Node, name: String, listener: js.Function1[Event, _]): Unit =
    node.addEventListener(name, listener)

  override def appendChild(parent: Element, child: Node): Unit = parent.appendChild(child)

  override def createElement(name: String): Element = document.createElement(name)

  override def createElementNS(namespace: String, name: String): Element =
    document.createElementNS(namespace, name)

  override def createTextNode(value: String): Text = document.createTextNode(value)

  override def childAt(element: Element, index: Int): Option[Node] =
    Option(element.childNodes.apply(index))

  override def children(element: Element): List[Node] = {
    val result = collection.mutable.ListBuffer.empty[Node]

    val childNodes = element.childNodes

    (0 until childNodes.length).foreach(index => result += childNodes.apply(index))

    result.toList
  }

  override def data(text: Text, value: String): Unit = text.data = value

  override val document: Document = dom.document

  override def getAttribute(element: Element, key: String): Option[String] =
    Option(element.getAttribute(key)).filter(_.nonEmpty)

  override def getElementById(id: String): Option[Element] =
    Option(document.getElementById(id))

  override val head: Element = document.head

  override def innerHtml(element: Element, value: String): Unit = element.innerHTML = value

  override def insertBefore(parent: Element, node: Node, reference: Option[Node]): Unit =
    parent.insertBefore(node, reference.orNull)

  override def parentNode(node: Node): Option[Node] = Option(node.parentNode)

  override def removeAttribute(element: Element, key: String): Unit = element.removeAttribute(key)

  override def removeChild(parent: Element, child: Node): Unit = parent.removeChild(child)

  override def removeEventListener(node: Node, name: String, listener: js.Function1[Event, _]): Unit =
    node.removeEventListener(name, listener)

  override def replaceChild(parent: Element, current: Node, next: Node): Unit =
    parent.replaceChild(next, current)

  override def setAttribute(element: Element, key: String, value: String): Unit =
    element.setAttribute(key, value)
}