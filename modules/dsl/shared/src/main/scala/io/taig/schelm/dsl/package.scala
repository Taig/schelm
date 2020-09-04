package io.taig.schelm

import io.taig.schelm.css.data.{Stylesheet, StylesheetNode, StylesheetWidget}
import io.taig.schelm.data.{Attribute, Listener, Node, Widget}

package object dsl extends AttributeDsl with ContextDsl with ListenerDsl with NodeDsl with StylesheetDsl {
  type DslWidget[+F[+_], +Event, -Context] = Widget[Context, StylesheetNode[Event, F[StylesheetWidget[Event, Context]]]]

  object DslWidget {
    def toStylesheetWidget[Event, Context](
        widget: DslWidget[Node[Event, +*], Event, Context]
    ): StylesheetWidget[Event, Context] =
      StylesheetWidget(widget)
  }

  implicit class AttributeKeySyntax(key: Attribute.Key) {
    def :=(value: String): Attribute = Attribute(key, Attribute.Value(value))
  }

  implicit class ListenerNameSyntax(name: Listener.Name) {
    def :=[Event](action: Listener.Action[Event]): Listener[Event] = Listener(name, action)
  }

  implicit class StylesheetRuleNameSyntax(name: Stylesheet.Rule.Name) {
    def :=(value: String): Stylesheet.Rule = Stylesheet.Rule(name, Stylesheet.Rule.Value(value))
  }
}
