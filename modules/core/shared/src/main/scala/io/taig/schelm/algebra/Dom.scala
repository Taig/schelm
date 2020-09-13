package io.taig.schelm.algebra

import io.taig.schelm.internal.Platform

abstract class Dom {
  def addEventListener(node: Dom.Node, name: String, listener: Dom.Listener): Unit

  def appendChild(parent: Dom.Element, child: Dom.Node): Unit

  def createElement(name: String): Dom.Element

  def createElementNS(namespace: String, name: String): Dom.Element

  def createTextNode(value: String): Dom.Text

  def childAt(element: Dom.Element, index: Int): Option[Dom.Node]

  def children(element: Dom.Element): List[Dom.Node]

  def data(text: Dom.Text, value: String): Unit

  def document: Dom.Document

  def insertBefore(parent: Dom.Element, node: Dom.Node, reference: Option[Dom.Node]): Unit

  def getAttribute(element: Dom.Element, key: String): Option[String]

  def getElementById(id: String): Option[Dom.Element]

  def head: Dom.Element

  def innerHtml(element: Dom.Element, value: String): Unit

  def parentNode(node: Dom.Node): Option[Dom.Node]

  def removeAttribute(element: Dom.Element, key: String): Unit

  def removeChild(parent: Dom.Element, child: Dom.Node): Unit

  def removeEventListener(node: Dom.Node, name: String, listener: Dom.Listener): Unit

  def replaceChild(parent: Dom.Element, current: Dom.Node, next: Dom.Node): Unit

  def setAttribute(element: Dom.Element, key: String, value: String): Unit
}

object Dom extends Platform {
  type Node

  type Element <: Node

  type Text <: Node

  type Document <: Node

  type Listener
}
