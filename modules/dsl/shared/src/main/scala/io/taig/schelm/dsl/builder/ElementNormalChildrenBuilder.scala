//package io.taig.schelm.dsl.builder
//
//import io.taig.schelm.css.data.{StylesheetHtml, StyledWidget}
//import io.taig.schelm.data.{Children, Element, Tag, Widget}
//
//final class ElementNormalChildrenBuilder[Event](val tag: Tag[Event]) extends AnyVal {
//  def apply[Context](children: StyledWidget[Event, Context]*): StyledWidget[Event, Context] =
//    StyledWidget(Widget.Pure(StylesheetHtml.Unstyled(Element.Normal(tag, Children.Indexed(children.toList)))))
//}
