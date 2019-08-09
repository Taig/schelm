package io.taig.schelm

import cats.data.Ior
import cats.implicits._

package object css extends NormalizeCss {
  type StyledHtmlDiff[A] = Ior[HtmlDiff[A], StylesheetDiff]

  type StyledWidget[+A] = Widget[A, Unit, Styles]

  object StylesWidget {
    def apply[A](
        component: Component[StyledWidget[A], A],
        styles: Styles
    ): StyledWidget[A] = Widget(styles, _ => component)
  }

  type StylesheetWidget[+A] = Widget[A, Unit, Stylesheet]

  object StylesheetWidget {
    def apply[A](
        component: Component[StylesheetWidget[A], A],
        stylesheet: Stylesheet
    ): StylesheetWidget[A] = Widget.pure(stylesheet, component)

    def empty[Event](
        component: Component[StylesheetWidget[Event], Event]
    ): StylesheetWidget[Event] =
      StylesheetWidget(component, Stylesheet.Empty)
  }

  def toStylesheetWidget[A](widget: StyledWidget[A]): StylesheetWidget[A] =
    widget.component match {
      case component: Component.Element[StyledWidget[A], A] =>
        val styles = widget.payload.map { style =>
          val identifier = Identifier(style.hashCode)
          val selectors = Selectors.of(identifier.selector)
          identifier -> style.toStylesheet(selectors)
        }.toMap

        val identifiers = styles.keys.toList
        val stylesheet = styles.values.toList.combineAll

        val attributes =
          if (identifiers.isEmpty) component.attributes
          else component.attributes.merge(cls(identifiers.map(_.cls)))

        val children =
          component.children.map((_, child) => toStylesheetWidget(child))

        StylesheetWidget(
          component.copy(attributes = attributes, children = children),
          stylesheet
        )
      case component: Component.Fragment[StyledWidget[A]] =>
        val children =
          component.children.map((_, child) => toStylesheetWidget(child))
        StylesheetWidget.empty(component.copy(children = children))
      case component: Component.Lazy[StyledWidget[A]] =>
        val eval = component.eval.map(toStylesheetWidget[A])
        StylesheetWidget.empty(component.copy(eval = eval))
      case component: Component.Text => StylesheetWidget.empty(component)
    }

  private def cls(values: List[String]): Attribute =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
