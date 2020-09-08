package io.taig.schelm.internal

import scala.scalajs.js

import io.taig.schelm.data.Platform
import org.scalajs.dom

trait DomPlatform {
  final type Node = dom.Node

  final type Element = dom.Element

  final type Text = dom.Text

  final type Listener = js.Function1[dom.Event, _]

  final val platform: Platform = Platform.Js
}
