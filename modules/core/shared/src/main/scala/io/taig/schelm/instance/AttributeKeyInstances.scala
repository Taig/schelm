package io.taig.schelm.instance

import cats.Order
import io.taig.schelm.data.Attribute

trait AttributeKeyInstances {
  implicit val attributeKeyOrder: Order[Attribute.Key] = Order.by(_.value)
}
