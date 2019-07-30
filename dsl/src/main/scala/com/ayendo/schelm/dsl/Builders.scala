package com.ayendo.schelm.dsl

import com.ayendo.schelm._
import com.ayendo.schelm.css.Widget

final class NodeBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(
      property: Attribute[A],
      properties: Attribute[A]*
  ): ChildrenBuilder[A] =
    new ChildrenBuilder(
      widget.setAttributes(Attributes.from(property +: properties))
    )

  def apply(child: Widget[A], children: Widget[A]*): Widget[A] =
    new ChildrenBuilder(widget)(child, children: _*)

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    new ChildrenBuilder(widget)(children)
}

final class TagBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(property: Attribute[A], properties: Attribute[A]*): Widget[A] =
    widget.setAttributes(Attributes.from(property +: properties))
}

final class ChildrenBuilder[A](val widget: Widget[A]) extends AnyVal {
  def apply(child: Widget[A], children: Widget[A]*): Widget[A] =
    widget.setChildren(Children.indexed(child +: children))

  def apply(children: List[(String, Widget[A])]): Widget[A] =
    widget.setChildren(Children.identified(children))
}
