package io.taig.schelm.playground

import io.taig.schelm.css.data.Stylesheet
import io.taig.schelm.data._

object Shared {
  final case class Theme()

  sealed abstract class Event extends Product with Serializable

  val widget: Component[Event, Theme, Stylesheet] = Component[Event, Theme, Stylesheet](
    _ =>
      Element.Normal(
        Tag("p", Attributes.Empty, Listeners.Empty),
        Children.Indexed(
          List(
            Component[Event, Theme, Stylesheet](_ => Text("yolo", Listeners.Empty), identity, Stylesheet.Empty)
          )
        )
      ),
    patch = identity,
    payload = Stylesheet.Empty
  )

  val component: Component[Event, Stylesheet] = widget.apply(Theme())
}
