package io.taig.schelm.playground

import io.taig.schelm.css.data.Stylesheet
import io.taig.schelm.data.{Component, Listeners, Text, Widget}

object Shared {
  final case class Theme()

  sealed abstract class Event extends Product with Serializable

  val widget: Widget[Event, Theme, Stylesheet] = Widget[Event, Theme, Stylesheet](
    _ => Text("yolo", Listeners.Empty),
    patch = identity,
    payload = Stylesheet.Empty
  )

  val component: Component[Event, Stylesheet] = widget.apply(Theme())
}
