package com.ayendo.schelm.css

import cats.Monoid
import cats.implicits._

import scala.collection.immutable.ListSet

final case class Styles(values: ListSet[Style]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def :+(style: Style): Styles = Styles(values + style)

  def ++(styles: Styles): Styles = Styles(values ++ styles.values)

  def map[A](f: Style => A): List[A] = values.toList.map(f)

  def foldMap[A: Monoid](f: Style => A): A = values.toList.foldMap(f)
}

object Styles {
  val Empty: Styles = Styles(ListSet.empty)

  def of(styles: Style*): Styles = Styles(ListSet.from(styles))

  def from(styles: Iterable[Style]): Styles = Styles(ListSet.from(styles))

  implicit val monoid: Monoid[Styles] = new Monoid[Styles] {
    override def empty: Styles = Empty

    override def combine(x: Styles, y: Styles): Styles = x ++ y
  }
}
