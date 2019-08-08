package io.taig.schelm

import cats.data.Ior
import cats.implicits._

package object css extends NormalizeCss {
  type StyledHtmlDiff[A] = Ior[HtmlDiff[A], StylesheetDiff]

  type Widget[+A] = Document[A, Styles]

  object Widget {
    def apply[A](
        component: Component[Widget[A], A],
        styles: Styles
    ): Widget[A] = Document(component, styles)
  }

  implicit final class WidgetSyntax[A](widget: Widget[A])
      extends ComponentOps[Widget, A](
        widget,
        _.tail,
        (component, widget) => Widget[A](component, widget.head)
      ) {
    def setStyles(styles: Styles): Widget[A] = Widget(widget.tail, styles)
  }

  type StyledHtml[+Event] = Document[Event, Stylesheet]

  object StyledHtml {
    def apply[Event](
        component: Component[StyledHtml[Event], Event],
        stylesheet: Stylesheet
    ): StyledHtml[Event] =
      Document(component, stylesheet)

    def empty[Event](
        component: Component[StyledHtml[Event], Event]
    ): StyledHtml[Event] =
      StyledHtml(component, Stylesheet.Empty)
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
      case component: Component.Fragment[Widget[Event]] =>
        val children = component.children.map((_, child) => toStyledHtml(child))
        StyledHtml.empty(component.copy(children = children))
      case component: Component.Lazy[Widget[Event]] =>
        val eval = component.eval.map(toStyledHtml[Event])
        StyledHtml.empty(component.copy(eval = eval))
      case component: Component.Text =>
        StyledHtml(component, Stylesheet.Empty)
    }

  def toStylesheet[A](html: StyledHtml[A]): Stylesheet =
    html.tail match {
      case component: Component.Element[StyledHtml[A], A] =>
        component.children.foldLeft(html.head) { (stylesheet, A, html) =>
          stylesheet |+| toStylesheet(html)
        }
      case component: Component.Fragment[StyledHtml[A]] =>
        component.children.foldLeft(Stylesheet.Empty) { (stylesheet, _, html) =>
          stylesheet |+| toStylesheet(html)
        }
      case component: Component.Lazy[StyledHtml[A]] =>
        toStylesheet(component.eval.value)
      case _: Component.Text => Stylesheet.Empty
    }

  private def cls(values: List[String]): Attribute =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
