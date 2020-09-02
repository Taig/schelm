package io.taig.schelm.dsl

import io.taig.schelm.css.data.Stylesheet
import io.taig.schelm.data._

trait Widgets {
  final def element(name: String): Component[Nothing, Any, Stylesheet] =
    Component(
      _ => Element.Normal(Tag(name, Attributes.Empty, Listeners.Empty), Children.Empty),
      identity,
      Stylesheet.Empty
    )

  final val div: Component[Nothing, Any, Stylesheet] = element("div")
}
