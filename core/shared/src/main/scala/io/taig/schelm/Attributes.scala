package io.taig.schelm

import cats._
import cats.data.Ior
import cats.implicits._

import scala.collection.immutable.ListMap
import scala.collection.mutable

final case class Attributes[A](
    values: ListMap[String, Property[A]]
) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def +(attribute: Attribute[A]): Attributes[A] =
    updated(attribute.key, attribute.property)

  def updated(key: String, property: Property[A]): Attributes[A] =
    Attributes(values.updated(key, property))

  def ++(properties: Attributes[A]): Attributes[A] =
    Attributes(values ++ properties.values)

  def map[B](f: Attribute[A] => B): List[B] = toList.map(f)

  /**
    * Merge the `Attribute` with an existing `Value.Multiple`
    *
    * If no `Value` for this key exists it will be added.
    */
  def merge(attribute: Attribute[A]): Attributes[A] = {
    val key = attribute.key
    val update = (values.get(key), attribute.property) match {
      case (Some(left: Value), right: Value) => left |+| right
      case _                                 => attribute.property
    }

    Attributes(values.updated(key, update))
  }

  def zipAll(
      attributes: Attributes[A]
  ): List[(String, Ior[Property[A], Property[A]])] = {
    val result =
      mutable.LinkedHashMap.empty[String, Ior[Property[A], Property[A]]]

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

  def toList: List[Attribute[A]] =
    values.map(Attribute.apply[A] _ tupled).toList

  def traverse_[F[_]: Applicative, B](f: Attribute[A] => F[B]): F[Unit] =
    toList.traverse_(f)
}

object Attributes {
  def empty[A]: Attributes[A] = Attributes(ListMap.empty)

  def of[A](values: Attribute[A]*): Attributes[A] = from(values)

  def from[A](values: Iterable[Attribute[A]]): Attributes[A] =
    Attributes(
      ListMap(
        values.map(attribute => attribute.key -> attribute.property).toSeq: _*
      )
    )

  implicit def monoid[A]: Monoid[Attributes[A]] = new Monoid[Attributes[A]] {
    override def empty: Attributes[A] = Attributes.empty

    override def combine(x: Attributes[A], y: Attributes[A]): Attributes[A] =
      x ++ y
  }
}
