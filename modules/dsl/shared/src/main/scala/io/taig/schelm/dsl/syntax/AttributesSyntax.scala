package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.operation.AttributesOperation

trait AttributesSyntax[+F[-_], -Context] { self =>
  final def attrs(attributes: Attribute*): F[Context] =
    self.attributes.patch(_ ++ Attributes.from(attributes))

  def attributes: AttributesOperation[F, Context]
}
