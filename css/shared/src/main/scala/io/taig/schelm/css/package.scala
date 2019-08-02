package io.taig.schelm

import cats._
import cats.implicits._

package object css extends NormalizeCss {
  type Widget[+A] = Cofree[Component[+?, A], Styles]

  object Widget {
    def apply[A](
        component: Component[Widget[A], A],
        styles: Styles
    ): Widget[A] =
      Cofree[Component[+?, A], Styles](styles, component)

    def render[F[_]: Monad, A](
        widget: Widget[A],
        registry: CssRegistry[F]
    ): F[Html[A]] =
      widget.tail match {
        case component: Component.Fragment[Widget[A], A] =>
          component.children
            .traverse((_, widget) => render(widget, registry))
            .map(children => Html(Component.Fragment(children)))
        case component: Component.Element[Widget[A], A] =>
          for {
            identifiers <- registry.register(widget.head)
            children <- component.children.traverse { (_, widget) =>
              render(widget, registry)
            }
          } yield {
            val update =
              if (identifiers.isEmpty) component.attributes
              else component.attributes.merge(cls(identifiers.map(_.cls)))
            Html(Component.Element(component.name, update, children))
          }
        case component: Component.Text => Html[A](component).pure[F]
      }

    def html[A](widget: Widget[A]): Html[A] =
      widget.tail match {
        case component: Component.Fragment[Widget[A], A] =>
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

    def render[F[_]: Monad](registry: CssRegistry[F]): F[Html[A]] =
      Widget.render(widget, registry)

    def html: Html[A] = Widget.html(widget)
  }

  private def cls[A](values: List[String]): Attribute[A] =
    Attribute("class", Value.Multiple(values, Accumulator.Whitespace))
}
