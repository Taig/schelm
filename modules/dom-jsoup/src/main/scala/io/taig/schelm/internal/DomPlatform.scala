package io.taig.schelm.internal

import io.taig.schelm.data.Platform
import org.jsoup.nodes.{Element => JElement, Node => JNode, TextNode => JText}

trait DomPlatform {
  final type Node = JNode

  final type Element = JElement

  final type Text = JText

  final type Listener = Unit

  final val platform: Platform = Platform.Jvm
}
