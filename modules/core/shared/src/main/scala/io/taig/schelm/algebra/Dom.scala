package io.taig.schelm.algebra

import io.taig.schelm.data.Listener.Action
import io.taig.schelm.internal.DomPlatform

abstract class Dom[F[_]] {
  // def callback(action: Action[Event]): Dom.Listener

  def addEventListener(node: Dom.Node, name: String, listener: Dom.Listener): F[Unit]

  def appendChild(parent: Dom.Element, child: Dom.Node): F[Unit]

  def createElement(name: String): F[Dom.Element]

  def createElementNS(namespace: String, name: String): F[Dom.Element]

  def createTextNode(value: String): F[Dom.Text]

  def childAt(element: Dom.Element, index: Int): F[Option[Dom.Node]]

  def children(element: Dom.Element): F[List[Dom.Node]]

  def data(text: Dom.Text, value: String): F[Unit]

  def insertBefore(parent: Dom.Element, node: Dom.Node, reference: Option[Dom.Node]): F[Unit]

  def getAttribute(element: Dom.Element, key: String): F[Option[String]]

  def getElementById(id: String): F[Option[Dom.Element]]

  def head: F[Dom.Element]

  def innerHtml(element: Dom.Element, value: String): F[Unit]

  def parentNode(node: Dom.Node): F[Option[Dom.Node]]

  def removeAttribute(element: Dom.Element, key: String): F[Unit]

  def removeChild(parent: Dom.Element, child: Dom.Node): F[Unit]

  def removeEventListener(node: Dom.Node, name: String, listener: Dom.Listener): F[Unit]

  def replaceChild(parent: Dom.Element, current: Dom.Node, next: Dom.Node): F[Unit]

  def setAttribute(element: Dom.Element, key: String, value: String): F[Unit]
}

object Dom extends DomPlatform
