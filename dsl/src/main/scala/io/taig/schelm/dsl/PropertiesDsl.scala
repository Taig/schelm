package io.taig.schelm.dsl

import io.taig.schelm.css.{Declaration, Declarations}
import io.taig.schelm._

trait PropertiesDsl {
  def attr(key: String, value: String): Attribute[Nothing] =
    Attribute(key, Value.One(value))

  def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Attribute[Nothing] = Attribute(key, Value.Multiple(values, accumulator))

  def data(key: String, value: String): Attribute[Nothing] =
    attr(s"data-$key", value)

  def flag(key: String, value: Boolean): Attribute[Nothing] =
    Attribute(key, Value.Flag(value))

  def cls(values: String*): Attribute[Nothing] =
    attrs("class", values.toList, Accumulator.Whitespace)

  def disabled(value: Boolean): Attribute[Nothing] = flag("disabled", value)

  val disabled: Attribute[Nothing] = disabled(true)

  def href(value: String): Attribute[Nothing] = attr("href", value)

  def id(value: String): Attribute[Nothing] = attr("id", value)

  def onClick[A](value: A): Attribute[A] =
    Attribute("click", Listener.Pure(value))

  def style(declarations: Declarations): Attribute[Nothing] =
    attrs(
      "style",
      declarations.rows,
      Accumulator.Semicolon + Accumulator.Whitespace
    )

  def style(declarations: Declaration*): Attribute[Nothing] =
    style(Declarations.from(declarations))
}
