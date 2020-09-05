package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.data.Tagged.@@
import io.taig.schelm.dsl.operation.AttributesOperation

trait AttributesSyntax[Event, Context, Tag] { self =>
  final def attrs(attributes: Attribute*): CssWidget[Event, Context] @@ Tag =
    self.attributes.patch(_ ++ Attributes.from(attributes))

  def attributes: AttributesOperation[Event, Context, Tag]
}
