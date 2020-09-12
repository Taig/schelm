package io.taig.schelm.dsl

import io.taig.schelm.css.data._
import io.taig.schelm.data._
import io.taig.schelm.dsl.internal.Tagged.@@
import io.taig.schelm.dsl.internal.{Has, Tagged}

trait NodeDsl {
//  implicit def normalElementSyntax[Event, Context](
//      widget: CssWidget[Event, Context] @@ NormalElementSyntax.Tag
//  ): NormalElementSyntax[Event, Context] =
//    new NormalElementSyntax[Event, Context](widget)
//
//  implicit def voidElementSyntax[Event, Context](
//      widget: CssWidget[Event, Context] @@ VoidElementSyntax.Tag
//  ): VoidElementSyntax[Event, Context] = new VoidElementSyntax[Event, Context](widget)
//
//  implicit def fragmentSyntax[Event, Context](
//      widget: CssWidget[Event, Context] @@ FragmentSyntax.Tag
//  ): FragmentSyntax[Event, Context] =
//    new FragmentSyntax[Event, Context](widget)
//
//  implicit def textSyntax[Event, Context](
//      widget: CssWidget[Event, Context] @@ TextSyntax.Tag
//  ): TextSyntax[Event, Context] = new TextSyntax[Event, Context](widget)

  def element(name: String): CssWidget[Nothing, Any] @@ Has.Attributes @@ Has.Css @@ Has.Listeners @@ Has.Children = {
    val element = Component.Element(
      Tag(name, Attributes.Empty, Listeners.Empty),
      Component.Element.Type.Normal(Children.Empty),
      Lifecycle.Empty
    )

    Tagged(CssWidget(Widget.Pure(CssNode.Unstyled(element))))
  }

//  final val fragment: CssWidget[Nothing, Any] @@ Has.Children =
//    Tagged(CssWidget(Widget.Pure(CssNode.Unstyled(Fragment(Children.Empty)))))
//
//  final def text(value: String): CssWidget[Nothing, Any] @@ Has.Listeners =
//    Tagged(CssWidget(Widget.Pure(CssNode.Unstyled(Text(value, Listeners.Empty)))))
//
//  def void(tag: String): CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css = {
//    val element = Element(Tag(tag, Attributes.Empty, Listeners.Empty), Element.Type.Void)
//    Tagged(CssWidget(Widget.Pure(CssNode.Unstyled(element))))
//  }

//  final val br: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css = void("br")
//
//  final val button: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css with Has.Children =
//    element("button")
//
//  final val div: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css with Has.Children = element(
//    "div"
//  )
//
//  final val hr: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css = void("hr")
//
//  final val p: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css with Has.Children =
//    element("p")
//
//  final val span: CssWidget[Nothing, Any] @@ Has.Attributes with Has.Listeners with Has.Css with Has.Children = element(
//    "span"
//  )
}
