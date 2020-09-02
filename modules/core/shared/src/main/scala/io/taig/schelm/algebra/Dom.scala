package io.taig.schelm.algebra

import io.taig.schelm.data.Listener.Action

abstract class Dom[F[_], Event] {
  type Node

  type Element <: Node

  type Text <: Node

  type Listener

  def callback(listener: Action[Event]): Listener

  def addEventListener(node: Node, name: String, listener: Listener): F[Unit]

  def appendChild(parent: Element, child: Node): F[Unit]

  def createElement(name: String): F[Element]

  def createElementNS(namespace: String, name: String): F[Element]

  def createTextNode(value: String): F[Text]

  def childAt(element: Element, index: Int): F[Option[Node]]

  def children(element: Element): F[List[Node]]

  def data(text: Text, value: String): F[Unit]

  def getAttribute(element: Element, key: String): F[Option[String]]

  def getElementById(id: String): F[Option[Element]]

  def head: F[Element]

  def innerHtml(element: Element, value: String): F[Unit]

  def removeAttribute(element: Element, key: String): F[Unit]

  def removeChild(parent: Element, child: Node): F[Unit]

  def removeEventListener(node: Node, name: String, listener: Listener): F[Unit]

  def setAttribute(element: Element, key: String, value: String): F[Unit]
}

object Dom {
  type Aux[F[_], Event, N, E <: N, T <: N] = Dom[F, Event] {
    type Node = N

    type Element = E

    type Text = T
  }

  type Node[F[_], Event, N] = Dom[F, Event] {
    type Node = N
  }
}
