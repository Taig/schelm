package io.taig.schelm

import cats.Applicative
import cats.implicits._

abstract class Dom[F[_], A, Node] {
  type Element <: Node

  type Text <: Node

  type Notify

  def lift(listener: Listener[A]): Notify

  def element(value: Node): F[Element]

  def text(value: Node): F[Text]

  def addEventListener(node: Node, name: String, notify: Notify): F[Unit]

  def appendChild(parent: Element, child: Node): F[Unit]

  final def appendChildren(parent: Element, children: List[Node])(
      implicit F: Applicative[F]
  ): F[Unit] =
    children.traverse_(appendChild(parent, _))

  def createElement(name: String): F[Element]

  def createTextNode(value: String): F[Text]

  def data(text: Text, value: String): F[Unit]

  def removeAttribute(element: Element, key: String): F[Unit]

  def removeChild(parent: Element, child: Node): F[Unit]

  final def removeChildren(
      parent: Element,
      children: List[Node]
  )(
      implicit F: Applicative[F]
  ): F[Unit] =
    children.traverse_(removeChild(parent, _))

  def setAttribute(element: Element, key: String, value: String): F[Unit]
}
