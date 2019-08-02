package io.taig.schelm

import cats.implicits._

package object css extends NormalizeCss {
  type Widget[+A] = Cofree[Component[+?, A], Styles]

  object Widget {
    def apply[A](
        component: Component[Widget[A], A],
        styles: Styles
    ): Widget[A] =
      Cofree[Component[+?, A], Styles](styles, component)

    def render[A](
        widget: Widget[A]
    ): (Html[A], Stylesheet) = {
      widget.tail match {
        case component: Component.Element[Widget[A], A] =>
          val styles = widget.head.map { style =>
            val identifier = Identifier(style.hashCode)
            val selectors = Selectors.of(identifier.selector)
            identifier -> style.toStylesheet(selectors)
          }.toMap

          val identifiers = styles.keys.toList
          val stylesheet = styles.values.toList.combineAll

          val attributes =
            if (identifiers.isEmpty) component.attributes
            else component.attributes.merge(cls(identifiers.map(_.cls)))

          val children = component.children.map((_, child) => render(child))

          val x = children.map { case (_, (html, _))     => html }
          val y = children.values.map { case (_, styles) => styles }

          val a = Html(component.copy(attributes = attributes, children = x))
          val b = stylesheet ++ y.combineAll

          (a, b)
        case component: Component.Fragment[Widget[A]] =>
          val x = component.children.map((_, widget) => render(widget))
          val y = x.map { case (_, (html, _))     => html }
          val z = x.values.map { case (_, styles) => styles }

          val a = Html(component.copy(children = y))
          val b = z.combineAll

          (a, b)
        case component: Component.Text => (Html(component), Stylesheet.Empty)
      }
    }

    def html[A](widget: Widget[A]): Html[A] =
      widget.tail match {
        case component: Component.Fragment[Widget[A]] =>
          Html(
            Component
              .Fragment(component.children.map((_, child) => html[A](child)))
          )
        case component: Component.Element[Widget[A], A] =>
          val children = component.children.map((_, child) => html[A](child))
          Html(component.copy(children = children))
        case component: Component.Text => Html(component)
      }
  }

  implicit final class WidgetSyntax[A](widget: Widget[A])
      extends ComponentOps[Widget, A](
        widget,
        _.tail,
        (component, widget) => Widget[A](component, widget.head)
      ) {
    def setStyles(styles: Styles): Widget[A] = Widget(widget.tail, styles)

    @inline
    def render: (Html[A], Stylesheet) = Widget.render(widget)

    @inline
    def html: Html[A] = Widget.html(widget)
  }

  private def cls[A](values: List[String]): Attribute[A] =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
