package io.taig.schelm

import io.taig.schelm.data.Attribute
import io.taig.schelm.dsl.keyword.{AttributeKeyword, ElementKeyword}

package object dsl extends ContextDsl with ElementKeyword with ListenerDsl with StylesheetDsl {
  implicit class AttributeKeySyntax(key: Attribute.Key) {
    def :=(value: String): Attribute = Attribute(key, Attribute.Value(value))

    def :=(value: Int): Attribute = Attribute(key, Attribute.Value.fromInt(value))

    def :=(value: Long): Attribute = Attribute(key, Attribute.Value.fromLong(value))

    def :=(values: Iterable[String]): Attribute = :=(values.mkString(" "))
  }

  object a extends AttributeKeyword

//  implicit class ListenerNameSyntax(name: Listener.Name) {
//    def :=[Event](action: Listener.Action[Event]): Listener[Event] = Listener(name, action)
//  }
//
//  implicit class StylesheetRuleNameSyntax(name: Declaration.Name) {
//    def :=(value: String): Declaration = Declaration(name, Declaration.Value(value))
//  }
}
