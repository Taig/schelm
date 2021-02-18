package io.taig.schelm.data

import scala.collection.immutable.ListMap
import scala.collection.mutable

import io.taig.schelm.util.Text

final case class Attributes(values: ListMap[Attribute.Key, Attribute.Value]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def merge(attributes: Attributes): Attributes = {
    val result = mutable.LinkedHashMap.from(values)

    attributes.values.foreach { case (key, value) =>
      result.updateWith(key) {
        case Some(current) => Some(current ++ value)
        case None          => Some(value)
      }
    }

    Attributes(result.to(ListMap))
  }

  def contains(key: Attribute.Key): Boolean = values.contains(key)

  def updated(key: Attribute.Key, value: Attribute.Value): Attributes = Attributes(values.updated(key, value))

  def ++(attributes: Attributes): Attributes = Attributes(values ++ attributes.values)

  def +(attribute: Attribute): Attributes = merge(Attributes.of(attribute))

  def -(key: Attribute.Key): Attributes = Attributes(values.removed(key))

  def toList: List[Attribute] = values.toList.map { case (key, value) => Attribute(key, value) }

  override def toString: String = if (isEmpty) s"[]"
  else
    s"""{
       |  ${Text.align(2)(toList.mkString(",\n"))}
       |}""".stripMargin
}

object Attributes {
  val Empty: Attributes = Attributes(ListMap.empty)

  def from(attributes: Iterable[Attribute]): Attributes = Attributes(attributes.map(_.toTuple).to(ListMap))

  def of(attributes: Attribute*): Attributes = from(attributes)
}
