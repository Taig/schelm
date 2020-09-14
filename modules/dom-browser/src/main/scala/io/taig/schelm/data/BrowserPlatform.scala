package io.taig.schelm.data

import org.scalajs.dom

import scala.scalajs.js

trait BrowserPlatform extends Platform {
  final override type Node = dom.Node
  final override type Element = dom.Element
  final override type Text = dom.Text
  final override type Document = dom.Document
  final override type Listener = js.Function1[dom.Event, _]
}
