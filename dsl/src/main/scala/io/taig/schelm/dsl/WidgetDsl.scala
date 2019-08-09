package io.taig.schelm.dsl

import io.taig.schelm.{Attributes, Children, Component, Listeners}
import io.taig.schelm.css.{Styles, Widget}

trait WidgetDsl extends Dsl[Widget, Property] {
  private val EmptyListeners: Listeners[Nothing] = Listeners.empty
  private val EmptyChildren: Children[Widget[Nothing]] = Children.empty

  private def split[A](
      properties: Iterable[Property[A]]
  ): (Attributes, Listeners[A], Styles) =
    // format: off
    properties.foldLeft((Attributes.Empty, Listeners.empty[A], Styles.Empty)) {
      case ((attributes, listeners, styles), Property.Attribute(attribute)) =>
        (attributes + attribute, listeners, styles)
      case ((attributes, listeners, styles), property: Property.Listener[A]) =>
        (attributes, listeners + property.value, styles)
      case ((attributes, listeners, styles), Property.Optional(property)) =>
        val (x, y, z) = split(property)
        (attributes ++ x, listeners ++ y, styles ++ z)
      case ((attributes, listeners, styles), Property.Styles(values)) =>
        (attributes, listeners, styles ++ values)
    }
  // format: on

  override protected def element(name: String): Widget[Nothing] =
    Widget(
      Component.Element[Widget[Nothing], Nothing](
        name,
        Attributes.Empty,
        EmptyListeners,
        EmptyChildren
      ),
      Styles.Empty
    )

  override def text(value: String): Widget[Nothing] =
    Widget(Component.Text(value), Styles.Empty)

  override protected def properties[A](
      component: Widget[A],
      properties: Iterable[Property[A]]
  ): Widget[A] = {
    val (attributes, listeners, styles) = split(properties)
    component
      .setAttributes(attributes)
      .setListeners(listeners)
      .setStyles(styles)
  }

  override protected def children[A](
      component: Widget[A],
      children: Children[Widget[A]]
  ): Widget[A] =
    component.setChildren(children)

  def stylesheet(styles: Styles): Property[Nothing] =
    Property.fromStyles(styles)

  def stylesheet(
      declaration: DeclarationOrPseudo,
      declarations: DeclarationOrPseudo*
  ): Property[Nothing] =
    stylesheet(styles(declaration, declarations: _*))
}

object WidgetDsl extends WidgetDsl
