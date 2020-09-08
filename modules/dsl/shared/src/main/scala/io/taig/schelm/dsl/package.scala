package io.taig.schelm

import io.taig.schelm.css.data.Declaration
import io.taig.schelm.data.{Attribute, Listener}

package object dsl extends AttributeDsl with ContextDsl with ListenerDsl with NodeDsl with StylesheetDsl {
  implicit class AttributeKeySyntax(key: Attribute.Key) {
    def :=(value: String): Attribute = Attribute(key, Attribute.Value(value))

    def :=(values: List[String]): Attribute = :=(values.mkString(" "))
  }

  implicit class ListenerNameSyntax(name: Listener.Name) {
    def :=[Event](action: Listener.Action[Event]): Listener[Event] = Listener(name, action)
  }

  implicit class StylesheetRuleNameSyntax(name: Declaration.Name) {
    def :=(value: String): Declaration = Declaration(name, Declaration.Value(value))
  }
}
