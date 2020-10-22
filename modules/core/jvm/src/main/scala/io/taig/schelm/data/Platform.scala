package io.taig.schelm.data

import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}

trait Platform {
  type Node = JNode

  type Element = JElement

  type Text = JText

  type Document = JDocument

  type Listener = Unit
}
