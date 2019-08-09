package io.taig.schelm.dsl

import io.taig.schelm.{Attributes, Children, Component, Listeners}
import io.taig.schelm.css.{Styles, Widget}

private object Cache {
  val EmptyListeners: Listeners[Nothing] = Listeners.empty
  val EmptyChildren: Children[Widget[Nothing]] = Children.empty
}

trait WidgetDsl extends Dsl[Widget] with CssDsl[Widget] {
  override def element(name: String): Widget[Nothing] =
    Widget(
      Component.Element[Widget[Nothing], Nothing](
        name,
        Attributes.Empty,
        Cache.EmptyListeners,
        Cache.EmptyChildren
      ),
      Styles.Empty
    )

  override def text(value: String): Widget[Nothing] =
    Widget(Component.Text(value), Styles.Empty)

  override protected def updateAttributes[A](
      component: Widget[A],
      f: Attributes => Attributes
  ): Widget[A] =
    component.updateAttributes(f)

  override protected def updateListeners[A](
      component: Widget[A],
      f: Listeners[A] => Listeners[A]
  ): Widget[A] =
    component.updateListeners(f)

  override protected def updateChildren[A](
      component: Widget[A],
      f: Children[Widget[A]] => Children[Widget[A]]
  ): Widget[A] =
    component.updateChildren(f)

  override protected def updateStyles[A](
      component: Widget[A],
      f: Styles => Styles
  ): Widget[A] =
    Widget(component.tail, f(component.head))
}

object WidgetDsl extends WidgetDsl
