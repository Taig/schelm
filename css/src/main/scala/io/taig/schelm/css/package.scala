package io.taig.schelm

import cats.data.Ior
import cats.implicits._

package object css extends NormalizeCss {
  type StyledHtmlDiff[A] = Ior[HtmlDiff[A], StylesheetDiff]

  type StyledWidget[+A] = Widget[A, Unit, Styles]

  type StylesheetWidget[+A] = Widget[A, Unit, Stylesheet]

  def toStylesheetWidget[A](widget: StyledWidget[A]): StylesheetWidget[A] =
    widget.component(unit) match {
      case component: Component.Element[StyledWidget[A], A] =>
        val styles = widget
          .payload(unit)
          .map { style =>
            val identifier = Identifier(style.hashCode)
            val selectors = Selectors.of(identifier.selector)
            identifier -> style.toStylesheet(selectors)
          }
          .toMap

        val identifiers = styles.keys.toList
        val stylesheet = styles.values.toList.combineAll

        val attributes =
          if (identifiers.isEmpty) component.attributes
          else component.attributes.merge(cls(identifiers.map(_.cls)))

        val children =
          component.children.map((_, child) => toStylesheetWidget(child))

        Widget.pure(
          component.copy(attributes = attributes, children = children),
          stylesheet
        )
      case component: Component.Fragment[StyledWidget[A]] =>
        val children =
          component.children.map((_, child) => toStylesheetWidget(child))
        Widget.empty(component.copy(children = children))
      case component: Component.Lazy[StyledWidget[A]] =>
        val eval = component.eval.map(toStylesheetWidget[A])
        Widget.empty(component.copy(eval = eval))
      case component: Component.Text => Widget.empty(component)
    }

  private def cls(values: List[String]): Attribute =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
