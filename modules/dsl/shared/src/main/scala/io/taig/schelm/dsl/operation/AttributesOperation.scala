package io.taig.schelm.dsl.operation

import io.taig.schelm.data.Attributes
import io.taig.schelm.dsl.data.DslWidget

abstract class AttributesOperation[F[+_], Event, Context] {
  final def set(attributes: Attributes): DslWidget[F, Event, Context] = patch(_ => attributes)

  def patch(f: Attributes => Attributes): DslWidget[F, Event, Context]
}
