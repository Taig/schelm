package io.taig.schelm.dsl

import io.taig.schelm.css._
import io.taig.schelm._

trait PropertiesDsl {
  def attr(key: String, value: String): Attribute =
    Attribute(key, Value.One(value))

  def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Attribute =
    Attribute(key, Value.Multiple(values, accumulator))

  def data(key: String, value: String): Attribute =
    attr(s"data-$key", value)

  def flag(key: String, value: Boolean): Attribute =
    Attribute(key, Value.Flag(value))

  def on[A](event: String, action: Action[A]): Listener[A] =
    Listener(event, action)

  def cls(values: String*): Attribute =
    attrs("class", values.toList, Accumulator.Whitespace)

  def disabled(value: Boolean): Attribute = flag("disabled", value)

  val disabled: Attribute = disabled(true)

  def href(value: String): Attribute = attr("href", value)

  def id(value: String): Attribute = attr("id", value)

  def onClick[A](value: A): Listener[A] = on("click", Action.Pure(value))

  def onSubmit[A](value: A): Listener[A] = on("submit", Action.Pure(value))

  def style(declarations: Declarations): Attribute =
    attrs(
      "style",
      declarations.rows,
      Accumulator.Semicolon + Accumulator.Whitespace
    )

  def style(
      declaration: Declaration,
      declarations: Declaration*
  ): Attribute =
    style(Declarations.from(declaration +: declarations))
}
