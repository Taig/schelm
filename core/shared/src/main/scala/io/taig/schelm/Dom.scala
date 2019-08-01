package io.taig.schelm

import cats.Applicative
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

abstract class Dom[F[_], A, Node] {
  type Element <: Node

  type Text <: Node

  type Notify

  def lift(listener: Listener[A]): Notify

  def element(value: Node): F[Element]

  def text(value: Node): F[Text]

  def addEventListener(node: Node, name: String, notify: Notify): F[Unit]

  def appendChild(parent: Node, child: Node): F[Unit]

  final def appendChildren(parent: Node, children: List[Node])(
      implicit F: Applicative[F]
  ): F[Unit] =
    children.traverse_(appendChild(parent, _))

  def createElement(name: String): F[Element]

  def createTextNode(value: String): F[Text]

  def removeAttribute(element: Element, key: String): F[Unit]

  def setAttribute(element: Element, key: String, value: String): F[Unit]
}
