package io.taig.schelm.dsl.syntax

import cats.implicits._
import io.taig.schelm.css.data.StylesheetWidget
import io.taig.schelm.data._
import io.taig.schelm.dsl.DslWidget
import io.taig.schelm.dsl.operation.{AttributesOperation, ChildrenOperation, ListenersOperation}

final class ElementNormalSyntax[Event, Context](widget: DslWidget[Element.Normal[Event, +*], Event, Context])
    extends AttributesSyntax[Element.Normal[Event, +*], Event, Context]
    with ChildrenSyntax[Element.Normal[Event, +*], Event, Context]
    with ListenersSyntax[Element.Normal[Event, +*], Event, Context] {
  override val attributes: AttributesOperation[Element.Normal[Event, +*], Event, Context] =
    new AttributesOperation[Element.Normal[Event, +*], Event, Context] {
      override def patch(f: Attributes => Attributes): DslWidget[Element.Normal[Event, +*], Event, Context] =
        widget.map(_.map(element => element.copy(tag = element.tag.copy(attributes = f(element.tag.attributes)))))
    }

  override val children: ChildrenOperation[Element.Normal[Event, +*], Event, Context] =
    new ChildrenOperation[Element.Normal[Event, +*], Event, Context] {
      override def patch(
          f: Children[StylesheetWidget[Event, Context]] => Children[StylesheetWidget[Event, Context]]
      ): DslWidget[Element.Normal[Event, +*], Event, Context] =
        widget.map(_.map(element => element.copy(children = f(element.children))))
    }

  override val listeners: ListenersOperation[Element.Normal[Event, +*], Event, Context] =
    new ListenersOperation[Element.Normal[Event, +*], Event, Context] {
      override def patch(
          f: Listeners[Event] => Listeners[Event]
      ): DslWidget[Element.Normal[Event, +*], Event, Context] =
        widget.map(_.map(element => element.copy(tag = element.tag.copy(listeners = f(element.tag.listeners)))))
    }
}
