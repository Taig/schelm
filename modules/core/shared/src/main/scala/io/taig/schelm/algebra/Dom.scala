package io.taig.schelm.algebra

abstract class Dom[F[_]] {
  type Node

  type Element <: Node

  type Text <: Node

  type Document <: Node

  type Listener

  def addEventListener(node: Node, name: String, listener: Listener): F[Unit]

  def appendChild(parent: Element, child: Node): F[Unit]

  def createElement(name: String): F[Element]

  def createElementNS(namespace: String, name: String): F[Element]

  def createTextNode(value: String): F[Text]

  def childAt(element: Element, index: Int): F[Option[Node]]

  def children(element: Element): F[List[Node]]

  def data(text: Text, value: String): F[Unit]

  def document: Document

  def insertBefore(parent: Element, node: Node, reference: Option[Node]): F[Unit]

  def getAttribute(element: Element, key: String): F[Option[String]]

  def getElementById(id: String): F[Option[Element]]

  def head: F[Element]

  def innerHtml(element: Element, value: String): F[Unit]

  def parentNode(node: Node): F[Option[Node]]

  def removeAttribute(element: Element, key: String): F[Unit]

  def removeChild(parent: Element, child: Node): F[Unit]

  def removeEventListener(node: Node, name: String, listener: Listener): F[Unit]

  def replaceChild(parent: Element, current: Node, next: Node): F[Unit]

  def setAttribute(element: Element, key: String, value: String): F[Unit]
}
