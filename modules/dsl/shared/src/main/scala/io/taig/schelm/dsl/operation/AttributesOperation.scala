package io.taig.schelm.dsl.operation

import cats.implicits._
import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.{Attributes, Component}
import io.taig.schelm.dsl.internal.Tagged
import io.taig.schelm.dsl.internal.Tagged.@@

final class AttributesOperation[F[_], Context, Tag](css: CssWidget[F, Context]) {
  def set(attributes: Attributes): CssWidget[F, Context] @@ Tag = patch(_ => attributes)

  def patch(f: Attributes => Attributes): CssWidget[F, Context] @@ Tag =
    Tagged(CssWidget(css.widget.map(_.map {
      case component @ Component.Element(tag, _, _) =>
        component.copy(tag = tag.copy(attributes = f(component.tag.attributes)))
      case component => component
    })))
}
