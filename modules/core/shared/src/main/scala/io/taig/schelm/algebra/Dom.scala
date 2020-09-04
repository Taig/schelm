package io.taig.schelm.algebra

import io.taig.schelm.data.Listener.Action

abstract class Dom[F[_], Event] {
  type Node

  type Element <: Node

  type Text <: Node

  type Listener

  def element(node: Node): Option[Element]

  def text(node: Node): Option[Text]

  def callback(action: Action[Event]): Listener

  def addEventListener(node: Node, name: String, listener: Listener): F[Unit]

  def appendChild(parent: Element, child: Node): F[Unit]

  def createElement(name: String): F[Element]

  def createElementNS(namespace: String, name: String): F[Element]

  def createTextNode(value: String): F[Text]

  def childAt(element: Element, index: Int): F[Option[Node]]

  def children(element: Element): F[List[Node]]

  def data(text: Text, value: String): F[Unit]

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

  def serialize(node: Node): String
}

object Dom {
  type Aux[F[_], Event, Node0, Element0, Text0] = Dom[F, Event] {
    type Node = Node0

    type Element = Element0

    type Text = Text0
  }

  type Node[F[_], Event, Node0] = Dom[F, Event] {
    type Node = Node0
  }

  type Element[F[_], Event, Element0] = Dom[F, Event] {
    type Element = Element0
  }
}
