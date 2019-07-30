package com.ayendo.schelm

import cats._
import cats.implicits._

package object css {
  type Widget[A] = Cofree[Component[?, A], Styles]

  object Widget {
    def apply[A](
        component: Component[Widget[A], A],
        styles: Styles
    ): Widget[A] =
      Cofree[Component[?, A], Styles](styles, component)

    def render[F[_]: Monad, A](
        widget: Widget[A],
        registry: StylesRegistry[F]
    ): F[Html[A]] =
      widget.tail match {
        case Component.Fragment(children) =>
          children
            .traverse((_, widget) => render(widget, registry))
            .map(children => Html(Component.Fragment(children)))
        case Component.Element(name, properties, children) =>
          for {
            identifiers <- registry.register(widget.head)
            children <- children.traverse { (_, widget) =>
              render(widget, registry)
            }
          } yield {
            val update =
              if (identifiers.isEmpty) properties
              else properties.merge(cls(identifiers.map(_.cls)))
            Html(Component.Element(name, update, children))
          }
        case html: Component.Text => Html[A](html).pure[F]
      }
  }

  implicit final class WidgetSyntax[A](widget: Widget[A])
      extends ComponentOps[Widget, A](
        widget,
        _.tail,
        (component, widget) => Widget[A](component, widget.head)
      ) {
    def setStyles(styles: Styles): Widget[A] = Widget(widget.tail, styles)
  }

  private def cls[A](values: List[String]): Attribute[A] =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
