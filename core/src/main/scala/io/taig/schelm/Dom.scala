package io.taig.schelm

import cats.Applicative
import cats.implicits._

abstract class Dom[F[_], Event] {
  type Notify

  def lift(listener: Action[Event]): Notify

  def element(value: Node): F[Element]

  def text(value: Node): F[Text]

  def addEventListener(
      node: Node,
      event: String,
      notify: Notify,
      path: Path
  ): F[Unit]

  def appendChild(parent: Element, child: Node): F[Unit]

  final def appendChildren(parent: Element, children: List[Node])(
      implicit F: Applicative[F]
  ): F[Unit] =
    children.traverse_(appendChild(parent, _))

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

  final def removeChildren(
      parent: Element,
      children: List[Node]
  )(
      implicit F: Applicative[F]
  ): F[Unit] =
    children.traverse_(removeChild(parent, _))

  def removeEventListener(node: Node, event: String, path: Path): F[Unit]

  def setAttribute(element: Element, key: String, value: String): F[Unit]
}
