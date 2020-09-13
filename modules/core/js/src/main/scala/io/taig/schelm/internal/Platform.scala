package io.taig.schelm.internal

import scala.scalajs.js

import org.scalajs.dom

trait Platform {
  type Node = dom.Node

  type Element = dom.Element

  type Text = dom.Text

  type Document = dom.Document

  type Listener = js.Function1[dom.Event, _]
}
