package io.taig.schelm.data

import scala.collection.mutable

final case class Attributes(values: Map[Attribute.Key, Attribute.Value]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def ++(attributes: Attributes): Attributes = {
    val result = mutable.HashMap.from(values)

    attributes.values.foreach {
      case (key, value) =>
        result.updateWith(key) {
          case Some(current) => Some(current ++ value)
          case None          => Some(value)
        }
    }

    Attributes(result.toMap)
  }

  @inline
  def contains(key: Attribute.Key): Boolean = values.contains(key)

  def +(attribute: Attribute): Attributes = ++(Attributes.of(attribute))

  def -(key: Attribute.Key): Attributes = Attributes(values.removed(key))

  def toList: List[Attribute] = values.toList.map { case (key, value) => Attribute(key, value) }
}

object Attributes {
  val Empty: Attributes = Attributes(Map.empty)

  def from(attributes: Iterable[Attribute]): Attributes = Attributes(attributes.map(_.toTuple).toMap)

  def of(attributes: Attribute*): Attributes = from(attributes)
}
