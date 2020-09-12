package io.taig.schelm.algebra

abstract class Dom {
  type Node

  type Element <: Node

  type Text <: Node

  type Document <: Node

  type Listener

  def addEventListener(node: Node, name: String, listener: Listener): Unit

  def appendChild(parent: Element, child: Node): Unit

  def createElement(name: String): Element

  def createElementNS(namespace: String, name: String): Element

  def createTextNode(value: String): Text

  def childAt(element: Element, index: Int): Option[Node]

  def children(element: Element): List[Node]

  def data(text: Text, value: String): Unit

  def document: Document

  def insertBefore(parent: Element, node: Node, reference: Option[Node]): Unit

  def getAttribute(element: Element, key: String): Option[String]

  def getElementById(id: String): Option[Element]

  def head: Element

  def innerHtml(element: Element, value: String): Unit

  def parentNode(node: Node): Option[Node]

  def removeAttribute(element: Element, key: String): Unit

  def removeChild(parent: Element, child: Node): Unit

  def removeEventListener(node: Node, name: String, listener: Listener): Unit

  def replaceChild(parent: Element, current: Node, next: Node): Unit

  def setAttribute(element: Element, key: String, value: String): Unit
}
