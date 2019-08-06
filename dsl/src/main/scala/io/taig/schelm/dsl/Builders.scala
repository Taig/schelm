package io.taig.schelm.dsl

import io.taig.schelm._
import io.taig.schelm.css.Widget
import io.taig.schelm.Children

final class NodeBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(
      property: Property[A],
      properties: Property[A]*
  ): ChildrenBuilder[A] = {
    val (attributes, listeners) = split(property +: properties)
    val update = widget
      .setAttributes(Attributes.from(attributes))
      .setListeners(Listeners.from(listeners))
    new ChildrenBuilder(update)
  }

  def apply(child: Widget[A], children: Widget[A]*): Widget[A] =
    new ChildrenBuilder(widget)(child, children: _*)

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    new ChildrenBuilder(widget)(children)
}

final class ChildrenBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(child: Widget[A], children: Widget[A]*): Widget[A] =
    widget.setChildren(Children.indexed(child +: children))

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    widget.setChildren(Children.identified(children))
}
