package io.taig.schelm.instance

import cats.Order
import cats.kernel.Monoid
import io.taig.schelm.data.Attribute

trait AttributeValueInstances {
  implicit val attributeValueOrder: Order[Attribute.Value] = Order.by(_.value)

  implicit val attributeValueMonoid: Monoid[Attribute.Value] = new Monoid[Attribute.Value] {
    override def empty: Attribute.Value = Attribute.Value.Empty

    override def combine(x: Attribute.Value, y: Attribute.Value): Attribute.Value = x ++ y
  }
}
