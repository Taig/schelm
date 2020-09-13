package io.taig.schelm.interpreter

import scala.scalajs.js

import io.taig.schelm.algebra.Dom
import org.scalajs.dom
import org.scalajs.dom.Event

object BrowserDom extends Dom {
  override def addEventListener(node: dom.Node, name: String, listener: js.Function1[Event, _]): Unit =
    node.addEventListener(name, listener)

  override def appendChild(parent: dom.Element, child: dom.Node): Unit = parent.appendChild(child)

  override def createElement(name: String): dom.Element = document.createElement(name)

  override def createElementNS(namespace: String, name: String): dom.Element =
    document.createElementNS(namespace, name)

  override def createTextNode(value: String): dom.Text = document.createTextNode(value)

  override def childAt(element: dom.Element, index: Int): Option[dom.Node] =
    Option(element.childNodes.apply(index))

  override def children(element: dom.Element): List[dom.Node] = {
    val result = collection.mutable.ListBuffer.empty[dom.Node]

    val childNodes = element.childNodes

    (0 until childNodes.length).foreach(index => result += childNodes.apply(index))

    result.toList
  }

  override def data(text: dom.Text, value: String): Unit = text.data = value

  override val document: dom.Document = dom.document

  override def getAttribute(element: dom.Element, key: String): Option[String] =
    Option(element.getAttribute(key)).filter(_.nonEmpty)

  override def getElementById(id: String): Option[dom.Element] =
    Option(document.getElementById(id))

  override val head: dom.Element = dom.document.head

  override def innerHtml(element: dom.Element, value: String): Unit = element.innerHTML = value

  override def insertBefore(parent: dom.Element, node: dom.Node, reference: Option[dom.Node]): Unit =
    parent.insertBefore(node, reference.orNull)

  override def parentNode(node: dom.Node): Option[dom.Node] = Option(node.parentNode)

  override def removeAttribute(element: dom.Element, key: String): Unit = element.removeAttribute(key)

  override def removeChild(parent: dom.Element, child: dom.Node): Unit = parent.removeChild(child)

  override def removeEventListener(node: dom.Node, name: String, listener: js.Function1[Event, _]): Unit =
    node.removeEventListener(name, listener)

  override def replaceChild(parent: dom.Element, current: dom.Node, next: dom.Node): Unit =
    parent.replaceChild(next, current)

  override def setAttribute(element: dom.Element, key: String, value: String): Unit =
    element.setAttribute(key, value)
}
