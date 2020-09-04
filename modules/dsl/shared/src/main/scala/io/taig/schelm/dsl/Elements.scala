package io.taig.schelm.dsl

import io.taig.schelm.css.data._
import io.taig.schelm.data._
import cats.implicits._

trait Elements {
  def element(tag: String): StylesheetWidget[Element.Normal[Nothing, +*], Nothing, Any] = {
    StylesheetWidget(
      Widget.Pure(
        StylesheetNode.Unstyled(Element.Normal(Tag(tag, Attributes.Empty, Listeners.Empty), Children.Empty))
      )
    )
  }

  implicit class ElementNormalBuilders[Event, Context](widget: StylesheetWidget[Element.Normal[Event, +*], Event, Context]) {
    def apply(children: StylesheetWidget[Node[Event, +*], Event, Context]): StylesheetWidget[Element.Normal[Event, +*], Event, Context] =
      widget
  }

  def contextual[Context, Event](
      f: Context => StylesheetWidget[Element.Normal[Event, +*], Event, Any]
  ): StylesheetWidget[Element.Normal[Event, +*], Event, Context] =
    StylesheetWidget(Widget.Render(context => f(context).widget))

  final val button: StylesheetWidget[Element.Normal[Nothing, +*], Nothing, Any] = element("button")

//
//  final val div: ElementNormalBuilder = element("div")
//
//  final val p: ElementNormalBuilder = element("p")
}
