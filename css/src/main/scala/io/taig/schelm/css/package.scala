package io.taig.schelm

import cats.implicits._

package object css extends NormalizeCss {
  type Widget[+Event] = Cofree[Component[+?, Event], Styles]

  object Widget {
    def apply[Event](
        component: Component[Widget[Event], Event],
        styles: Styles
    ): Widget[Event] = Cofree[Component[+?, Event], Styles](styles, component)
  }

  implicit final class WidgetSyntax[A](widget: Widget[A])
      extends ComponentOps[Widget, A](
        widget,
        _.tail,
        (component, widget) => Widget[A](component, widget.head)
      ) {
    def setStyles(styles: Styles): Widget[A] = Widget(widget.tail, styles)
  }

  type StyledHtml[+Event] = Cofree[Component[+?, Event], Stylesheet]

  object StyledHtml {
    def apply[Event](
        component: Component[StyledHtml[Event], Event],
        stylesheet: Stylesheet
    ): StyledHtml[Event] =
      Cofree[Component[+?, Event], Stylesheet](stylesheet, component)
  }

  def toStyledHtml[Event](widget: Widget[Event]): StyledHtml[Event] =
    widget.tail match {
      case component: Component.Element[Widget[Event], Event] =>
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

        val children = component.children.map((_, child) => toStyledHtml(child))

        StyledHtml(
          component.copy(attributes = attributes, children = children),
          stylesheet
        )
      case Component.Fragment(children) =>
        StyledHtml(
          Component.Fragment(children.map((_, child) => toStyledHtml(child))),
          Stylesheet.Empty
        )
      case Component.Lazy(component, hash) =>
        StyledHtml(
          Component.Lazy(component.map(toStyledHtml[Event]), hash),
          Stylesheet.Empty
        )
      case component: Component.Text =>
        StyledHtml(component, Stylesheet.Empty)
    }

  private def cls[A](values: List[String]): Attribute[A] =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
