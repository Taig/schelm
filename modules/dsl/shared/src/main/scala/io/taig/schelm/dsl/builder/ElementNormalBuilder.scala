//package io.taig.schelm.dsl.builder
//
//import io.taig.schelm.css.data.{StylesheetHtml, StyledWidget}
//import io.taig.schelm.data._
//
//final class ElementNormalBuilder(val tag: Tag[Nothing]) extends AnyVal {
//  def attributes(attribute: Attribute, attributes: Attribute*): ElementNormalChildrenBuilder[Nothing] =
//    new ElementNormalChildrenBuilder[Nothing](tag.copy(attributes = Attributes(attribute +: attributes.toList)))
//
//  def listeners[Event](values: Listener[Event]*): ElementNormalChildrenBuilder[Event] =
//    new ElementNormalChildrenBuilder[Event](tag.copy(listeners = Listeners(values.toList)))
//
//  def apply[Event, Context](children: StyledWidget[Event, Context]*): StyledWidget[Event, Context] =
//    StyledWidget(Widget.Pure(StylesheetHtml.Unstyled(Element.Normal(tag, Children.Indexed(children.toList)))))
//}
