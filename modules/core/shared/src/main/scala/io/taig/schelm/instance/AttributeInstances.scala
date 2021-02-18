package io.taig.schelm.instance

import cats.Eq
import io.taig.schelm.data.Attribute

trait AttributeInstances {
  implicit val attributeEq: Eq[Attribute] = Eq.by(_.toTuple)
}
