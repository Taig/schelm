package io.taig.schelm.dsl.data

import io.taig.schelm.data.Attribute

final case class DslAttribute(key: Attribute.Key, value: Option[Attribute.Value])
