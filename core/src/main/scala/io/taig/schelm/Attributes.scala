package io.taig.schelm

import cats._
import cats.data.Ior
import cats.implicits._

import scala.collection.immutable.ListMap
import scala.collection.mutable

final case class Attributes(
    values: ListMap[String, Value]
) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def +(attribute: Attribute): Attributes =
    updated(attribute.key, attribute.value)

  def updated(key: String, value: Value): Attributes =
    Attributes(values.updated(key, value))

  def ++(properties: Attributes): Attributes =
    Attributes(values ++ properties.values)

  def map[B](f: Attribute => B): List[B] = toList.map(f)

  /**
    * Merge the `Attribute` with an existing `Value.Multiple`
    *
    * If no `Value` for this key exists it will be added.
    */
  def merge(attribute: Attribute): Attributes = {
    val key = attribute.key
    val update = (values.get(key), attribute.value) match {
      case (Some(left: Value), right: Value) => left |+| right
      case _                                 => attribute.value
    }

    Attributes(values.updated(key, update))
  }

  def zipAll(
      attributes: Attributes
  ): List[(String, Ior[Value, Value])] = {
    val result =
      mutable.LinkedHashMap.empty[String, Ior[Value, Value]]

    this.values
      .foreach { case (key, property) => result.put(key, Ior.left(property)) }

    attributes.values.foreach {
      case (key, property) =>
        val update = result
          .get(key)
          .map(_.putRight(property))
          .getOrElse(Ior.right(property))

        result.put(key, update)
    }

    result.toList
  }

  def toList: List[Attribute] =
    values.map(Attribute.tupled).toList

  def traverse_[F[_]: Applicative, B](f: Attribute => F[B]): F[Unit] =
    toList.traverse_(f)
}

object Attributes {
  val Empty: Attributes = Attributes(ListMap.empty)

  def of(values: Attribute*): Attributes = from(values)

  def from(values: Iterable[Attribute]): Attributes =
    Attributes(
      ListMap(
        values.map(attribute => attribute.key -> attribute.value).toSeq: _*
      )
    )

  implicit val monoid: Monoid[Attributes] = new Monoid[Attributes] {
    override def empty: Attributes = Empty

    override def combine(x: Attributes, y: Attributes): Attributes =
      x ++ y
  }
}
