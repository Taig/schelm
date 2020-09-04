package io.taig.schelm

import io.taig.schelm.css.data.{Stylesheet, StylesheetNode, StylesheetWidget}
import io.taig.schelm.data.{Attribute, Listeners, Text, Widget}

package object dsl extends Attributes with Elements with Stylesheets {
  implicit class AttributeKeySyntax(key: Attribute.Key) {
    def :=(value: String): Attribute = Attribute(key, Attribute.Value(value))
  }

  implicit class StylesheetRuleNameSyntax(name: Stylesheet.Rule.Name) {
    def :=(value: String): Stylesheet.Rule = Stylesheet.Rule(name, Stylesheet.Rule.Value(value))
  }

  type TextF[+A] = Text[Nothing]

  final def text(value: String): StylesheetWidget[TextF, Nothing, Any] =
    StylesheetWidget(
      Widget.Pure(
        StylesheetNode.Unstyled(Text(value, Listeners.Empty))
      )
    )

//  final def contextual[Context](render: Context => StyledWidget[Nothing, Context]): StyledWidget[Nothing, Context] =
//    StyledWidget(Widget.Render(render(_: Context).widget))
}
