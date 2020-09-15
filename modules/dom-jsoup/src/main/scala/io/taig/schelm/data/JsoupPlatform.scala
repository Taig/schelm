package io.taig.schelm.data

import org.jsoup.nodes.{Document => JDocument, Element => JElement, Node => JNode, TextNode => JText}

trait JsoupPlatform extends Platform {
  final override type Node = JNode

  final override type Element = JElement

  final override type Text = JText

  final override type Document = JDocument

  final override type Listener = Unit

  final override val isJs: Boolean = false

  final override val isJvm: Boolean = true
}
