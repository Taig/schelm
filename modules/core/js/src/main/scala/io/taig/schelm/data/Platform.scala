package io.taig.schelm.data

import org.scalajs.dom

import scala.scalajs.js

trait Platform {
  final type Node = dom.Node

  final type Element = dom.Element

  final type Text = dom.Text

  final type Document = dom.Document

  final type Listener = js.Function1[dom.Event, _]
}
