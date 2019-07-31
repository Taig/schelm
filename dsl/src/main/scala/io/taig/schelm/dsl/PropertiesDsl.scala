package io.taig.schelm.dsl

import io.taig.schelm.css.{Declaration, Declarations}
import io.taig.schelm._

trait PropertiesDsl[A] {
  def attr(key: String, value: String): Attribute[A] =
    Attribute(key, Value.One(value))

  def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Attribute[A] = Attribute(key, Value.Multiple(values, accumulator))

  def data(key: String, value: String): Attribute[A] = attr(s"data-$key", value)

  def flag(key: String, value: Boolean): Attribute[A] =
    Attribute(key, Value.Flag(value))

  def cls(values: String*): Attribute[A] =
    attrs("class", values.toList, Accumulator.Whitespace)

  def disabled(value: Boolean): Attribute[A] = flag("disabled", value)

  val disabled: Attribute[A] = disabled(true)

  def href(value: String): Attribute[A] = attr("href", value)

  def id(value: String): Attribute[A] = attr("id", value)

  def onClick(value: A): Attribute[A] = Attribute("click", Listener.Pure(value))

  def style(declarations: Declarations): Attribute[A] =
    attrs(
      "style",
      declarations.rows,
      Accumulator.Semicolon + Accumulator.Whitespace
    )

  def style(declarations: Declaration*): Attribute[A] =
    style(Declarations.from(declarations))
}
