package io.taig.schelm.data

import org.scalajs.dom
import scala.scalajs.js

import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLDocument

trait Platform {
  final type EventTarget = dom.EventTarget

  final type Node = dom.Node

  final type Element = dom.Element

  final type Text = dom.Text

  final type Document = HTMLDocument

  final type Listener = js.Function1[Event, _]

  final type Window = dom.Window
}
