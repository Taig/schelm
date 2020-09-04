package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.operation.AttributesOperation

trait AttributesSyntax[F[+_], Event, Context] { self =>
  final def attrs(attributes: Attribute*): DslWidget[F, Event, Context] =
    self.attributes.set(Attributes(attributes.toList))

  def attributes: AttributesOperation[F, Event, Context]
}
