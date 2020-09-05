package io.taig.schelm.data

final case class Attributes(values: Vector[Attribute]) extends AnyVal {
  def ++(attributes: Attributes): Attributes = Attributes {
    attributes.values.foldLeft(values) { (result, attribute) =>
      result.indexWhere(_.key == attribute.key) match {
        case -1    => values :+ attribute
        case index => values.updated(index, attribute.copy(value = values(index).value ++ attribute.value))
      }
    }
  }

  def :+(attribute: Attribute): Attributes = this ++ Attributes.of(attribute)

  def +:(attribute: Attribute): Attributes = Attributes.of(attribute) ++ this

  def -(key: Attribute.Key): Attributes = Attributes(values.filter(_.key != key))

  def toList: List[Attribute] = values.toList
}

object Attributes {
  val Empty: Attributes = Attributes(Vector.empty)

  def from(attributes: Iterable[Attribute]): Attributes = Attributes(attributes.toVector)

  def of(attributes: Attribute*): Attributes = Attributes(attributes.toVector)
}
