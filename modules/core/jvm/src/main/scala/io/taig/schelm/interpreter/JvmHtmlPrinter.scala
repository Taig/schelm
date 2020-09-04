package io.taig.schelm.interpreter

import io.taig.schelm.algebra.Printer
import org.jsoup.nodes.{Node => JNode}

object JvmHtmlPrinter extends Printer[List[JNode]] {
  override def print(structure: List[JNode]): String = structure.map(_.outerHtml()).mkString("\n")
}
