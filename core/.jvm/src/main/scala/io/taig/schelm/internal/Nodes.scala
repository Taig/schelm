package io.taig.schelm.internal

import org.jsoup.nodes.{Element => JElement, Node => JNode, TextNode => JText}

trait Nodes {
  type Node = JNode
  type Element = JElement
  type Text = JText
}
