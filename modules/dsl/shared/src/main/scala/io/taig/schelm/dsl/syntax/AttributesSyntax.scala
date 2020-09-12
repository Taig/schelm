package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.internal.Has
import io.taig.schelm.dsl.internal.Tagged.@@
import io.taig.schelm.dsl.operation.AttributesOperation

final class AttributesSyntax[F[_], Context, Tag](widget: CssWidget[F, Context] @@ Has.Attributes with Tag) { self =>
  def attrs(attributes: Attribute*): CssWidget[F, Context] @@ Tag =
    self.attributes.patch(_ ++ Attributes.from(attributes))

  def attributes: AttributesOperation[F, Context, Tag] = new AttributesOperation[F, Context, Tag](widget)
}
