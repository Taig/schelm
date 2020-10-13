package io.taig.schelm

import io.taig.schelm.css.data.Declaration
import io.taig.schelm.data.{Attribute, Listener}
import io.taig.schelm.dsl.keyword.{AttributeKeyword, NodeKeyword, StyleKeyword}

package object dsl extends ContextDsl with NodeKeyword with ListenerDsl with StyleKeyword {
  implicit class AttributeKeySyntax(key: Attribute.Key) {
    def :=(value: String): Attribute = Attribute(key, Attribute.Value(value))

    def :=(value: Int): Attribute = Attribute(key, Attribute.Value.fromInt(value))

    def :=(value: Long): Attribute = Attribute(key, Attribute.Value.fromLong(value))

    def :=(values: Iterable[String]): Attribute = :=(values.mkString(" "))
  }

  object a extends AttributeKeyword
  object e extends NodeKeyword

  implicit class ListenerNameSyntax(name: Listener.Name) {
    def :=[F[_]](action: Any => F[Unit]): Listener[F] = Listener(name, action)
  }

  implicit class StylesheetRuleNameSyntax(name: Declaration.Name) {
    def :=(value: String): Declaration = Declaration(name, Declaration.Value(value))
  }
}
