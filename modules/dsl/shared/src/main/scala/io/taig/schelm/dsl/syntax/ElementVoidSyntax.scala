package io.taig.schelm.dsl.syntax

import cats.implicits._
import io.taig.schelm.data.{Attributes, Element}
import io.taig.schelm.dsl.data.DslWidget
import io.taig.schelm.dsl.operation.AttributesOperation

final class ElementVoidSyntax[Event, Context](widget: DslWidget[λ[`+A` => Element.Void[Event]], Event, Context])
    extends AttributesSyntax[λ[`+A` => Element.Void[Event]], Event, Context] {
  override val attributes: AttributesOperation[λ[`+A` => Element.Void[Event]], Event, Context] =
    new AttributesOperation[λ[`+A` => Element.Void[Event]], Event, Context] {
      override def patch(f: Attributes => Attributes): DslWidget[λ[`+A` => Element.Void[Event]], Event, Context] =
        DslWidget[λ[`+A` => Element.Void[Event]], Event, Context](
          widget.widget
            .map(_.map(element => element.copy(tag = element.tag.copy(attributes = f(element.tag.attributes)))))
        )
    }
}
