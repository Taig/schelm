package io.taig.schelm.dsl

import io.taig.schelm.css.data._
import io.taig.schelm.data._
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.syntax.{ElementNormalSyntax, ElementVoidSyntax, FragmentSyntax}

trait NodeDsl {
  implicit def elementNormalSyntax[Event, Context](
      widget: DslWidget[Element.Normal[Event, +*], Event, Context]
  ): ElementNormalSyntax[Event, Context] =
    new ElementNormalSyntax[Event, Context](widget)

  implicit def elementVoidSyntax[Event, Context](
      widget: DslWidget[λ[`+A` => Element.Void[Event]], Event, Context]
  ): ElementVoidSyntax[Event, Context] =
    new ElementVoidSyntax[Event, Context](widget)

  implicit def fragmentSyntax[Event, Context](
      widget: DslWidget[Fragment[+*], Event, Context]
  ): FragmentSyntax[Event, Context] =
    new FragmentSyntax[Event, Context](widget)

  def element(tag: String): DslWidget[Element.Normal[Nothing, +*], Nothing, Any] = {
    val element = Element.Normal(Tag(tag, Attributes.Empty, Listeners.Empty), Children.Empty)
    DslWidget(Widget.Pure(StylesheetNode(element, Stylesheet.Empty)))
  }

  final val fragment: DslWidget[Fragment[+*], Nothing, Any] = DslWidget(
    Widget.Pure(StylesheetNode(Fragment(Children.Empty), Stylesheet.Empty))
  )

  final def text(value: String): DslWidget[λ[`+A` => Text[Nothing]], Nothing, Any] =
    DslWidget[λ[`+A` => Text[Nothing]], Nothing, Any](
      Widget.Pure(StylesheetNode(Text(value, Listeners.Empty), Stylesheet.Empty))
    )

  def void(tag: String): DslWidget[λ[`+A` => Element.Void[Nothing]], Nothing, Any] = {
    val element = Element.Void(Tag(tag, Attributes.Empty, Listeners.Empty))
    DslWidget[λ[`+A` => Element.Void[Nothing]], Nothing, Any](Widget.Pure(StylesheetNode(element, Stylesheet.Empty)))
  }

  final val br: DslWidget[λ[`+A` => Element.Void[Nothing]], Nothing, Any] = void("br")

  final val button: DslWidget[Element.Normal[Nothing, +*], Nothing, Any] = element("button")

  final val div: DslWidget[Element.Normal[Nothing, +*], Nothing, Any] = element("div")

  final val hr: DslWidget[λ[`+A` => Element.Void[Nothing]], Nothing, Any] = void("hr")

  final val p: DslWidget[Element.Normal[Nothing, +*], Nothing, Any] = element("p")
}
