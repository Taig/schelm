package io.taig.schelm.interpreter

import io.taig.schelm.algebra.Printer
import org.scalajs.dom

object JsHtmlPrinter extends Printer[List[dom.Node]] {
  override def print(nodes: List[dom.Node]): String =
    nodes.map {
      case element: dom.Element => element.outerHTML
      case node => node.innerText
    }.mkString("\n")
}
