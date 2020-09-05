package io.taig.schelm.css.data

import cats.data.NonEmptyList

final case class Selectors(values: NonEmptyList[Selector]) extends AnyVal {
  def +(modifier: Modifier): Selectors = Selectors(values.map(_ + modifier))
}

object Selectors {
  def of(selector: Selector, selectors: Selector*): Selectors = Selectors(NonEmptyList(selector, selectors.toList))
}
