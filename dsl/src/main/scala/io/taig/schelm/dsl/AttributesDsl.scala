package io.taig.schelm.dsl

import io.taig.schelm.css._
import io.taig.schelm._

trait AttributesDsl {
  final def attr(key: String, value: String): Attribute =
    Attribute(key, Value.One(value))

  final def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Attribute =
    Attribute(key, Value.Multiple(values, accumulator))

  final def data(key: String, value: String): Attribute = attr(s"data-$key", value)

  final def aria(key: String, value: String): Attribute = attr(s"aria-$key", value)

  final def flag(key: String, value: Boolean): Attribute =
    Attribute(key, Value.Flag(value))

  final def cls(values: String*): Attribute =
    attrs("class", values.toList, Accumulator.Whitespace)

  // format: off
  final def cx(value: String): Attribute = attr("cx", value)
  final def rx(value: String): Attribute = attr("rx", value)
  final def ry(value: String): Attribute = attr("ry", value)
  final def cy(value: String): Attribute = attr("cy", value)
  final def d(value: String): Attribute = attr("d", value)
  final def disabled(value: Boolean): Attribute = flag("disabled", value)
  final val disabled: Attribute = disabled(true)
  final def href(value: String): Attribute = attr("href", value)
  final def id(value: String): Attribute = attr("id", value)
  // format: on

  final def style(declarations: Declarations): Attribute =
    attrs("style", declarations.rows, Accumulator.Whitespace)

  final def style(declarations: Declaration*): Attribute =
    style(Declarations.from(declarations))
}
