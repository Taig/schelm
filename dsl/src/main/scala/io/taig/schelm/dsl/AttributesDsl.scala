package io.taig.schelm.dsl

import io.taig.schelm.css._
import io.taig.schelm._

trait AttributesDsl {
  def attr(key: String, value: String): Attribute =
    Attribute(key, Value.One(value))

  def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Attribute =
    Attribute(key, Value.Multiple(values, accumulator))

  def data(key: String, value: String): Attribute = attr(s"data-$key", value)

  def aria(key: String, value: String): Attribute = attr(s"aria-$key", value)

  def flag(key: String, value: Boolean): Attribute =
    Attribute(key, Value.Flag(value))

  def cls(values: String*): Attribute =
    attrs("class", values.toList, Accumulator.Whitespace)

  // format: off
  def cx(value: String): Attribute = attr("cx", value)
  def rx(value: String): Attribute = attr("rx", value)
  def ry(value: String): Attribute = attr("ry", value)
  def cy(value: String): Attribute = attr("cy", value)
  def d(value: String): Attribute = attr("d", value)
  def disabled(value: Boolean): Attribute = flag("disabled", value)
  val disabled: Attribute = disabled(true)
  def href(value: String): Attribute = attr("href", value)
  def id(value: String): Attribute = attr("id", value)
  // format: on

  def style(declarations: Declarations): Attribute =
    attrs("style", declarations.rows, Accumulator.Whitespace)

  def style(declarations: Declaration*): Attribute =
    style(Declarations.from(declarations))
}
