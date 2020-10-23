package io.taig.schelm.data

import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}
import org.scalajs.dom.raw.Event

trait Platform {
  final type Node = JNode

  final type Element = JElement

  final type Text = JText

  final type Document = JDocument

  final type Listener[E <: Event] = Unit
}
