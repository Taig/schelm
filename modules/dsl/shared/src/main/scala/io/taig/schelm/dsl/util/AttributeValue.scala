package io.taig.schelm.dsl.util

import simulacrum.typeclass
import cats.implicits._

@typeclass
trait AttributeValue[A] {
  def lift(value: A): Option[String]
}

object AttributeValue {
  implicit val string: AttributeValue[String] = value => Some(value)

  implicit def list[A](implicit evidence: AttributeValue[A]): AttributeValue[List[A]] = { values =>
    val attributes = values.mapFilter(evidence.lift)
    if (attributes.isEmpty) None else Some(attributes.mkString(" "))
  }

  implicit def option[A](implicit evidence: AttributeValue[A]): AttributeValue[Option[A]] = _.flatMap(evidence.lift)
}
