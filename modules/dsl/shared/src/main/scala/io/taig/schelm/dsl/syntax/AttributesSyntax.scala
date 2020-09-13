package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.operation.AttributesOperation

trait AttributesSyntax[+F[_], -Context, +A] { self =>
  final def attrs(attributes: Attribute*): A =
    self.attributes.patch(_ ++ Attributes.from(attributes))

  def attributes: AttributesOperation[F, Context, A]
}
