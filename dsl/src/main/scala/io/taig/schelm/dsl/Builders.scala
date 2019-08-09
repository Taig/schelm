package io.taig.schelm.dsl

import io.taig.schelm.Children
import io.taig.schelm.css.Widget

final class NodeBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(
      property: Property[A],
      properties: Property[A]*
  ): ChildrenBuilder[A] = {
    val (attributes, listeners, styles) = split(property +: properties)
    val update = widget
      .setAttributes(attributes)
      .setListeners(listeners)
      .setStyles(styles)
    new ChildrenBuilder(update)
  }

  def apply(children: Widget[A]*): Widget[A] =
    new ChildrenBuilder(widget)(children: _*)

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    new ChildrenBuilder(widget)(children)
}

final class ChildrenBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(children: Widget[A]*): Widget[A] =
    widget.setChildren(Children.indexed(children))

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    widget.setChildren(Children.identified(children))
}
