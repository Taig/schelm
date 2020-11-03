package io.taig.schelm.data

import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}

trait Platform {
  final type Node = JNode

  final type Element = JElement

  final type Text = JText

  final type Document = JDocument

  final type Listener = Unit
}
