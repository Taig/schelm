package io.taig.schelm.css

import cats.data.NonEmptyList
import cats.implicits._

final case class Selectors(values: NonEmptyList[Selector]) extends AnyVal {
  def +(modifier: Modifier): Selectors = Selectors(values.map(_ + modifier))

  override def toString: String = values.map(_.toString).mkString_(", ")
}

object Selectors {
  def of(selector: Selector, selectors: Selector*): Selectors =
    Selectors(NonEmptyList.of(selector, selectors: _*))

  def one(selector: Selector): Selectors = Selectors(NonEmptyList.one(selector))

  def from(selector: Selector, selectors: Iterable[Selector]): Selectors =
    Selectors(NonEmptyList(selector, selectors.toList))
}
