package io.taig.schelm.dsl

import io.taig.schelm.css._
import io.taig.schelm._

trait PropertiesDsl {
  def attr(key: String, value: String): Property[Nothing] =
    Property.fromAttribute(Attribute(key, Value.One(value)))

  def attrs(
      key: String,
      values: List[String],
      accumulator: Accumulator
  ): Property[Nothing] =
    Property.fromAttribute(Attribute(key, Value.Multiple(values, accumulator)))

  def data(key: String, value: String): Property[Nothing] =
    attr(s"data-$key", value)

  def flag(key: String, value: Boolean): Property[Nothing] =
    Property.fromAttribute(Attribute(key, Value.Flag(value)))

  def on[A](event: String, action: Action[A]): Property[A] =
    Property.fromListener(Listener(event, action))

  def cls(values: String*): Property[Nothing] =
    attrs("class", values.toList, Accumulator.Whitespace)

  def disabled(value: Boolean): Property[Nothing] = flag("disabled", value)

  val disabled: Property[Nothing] = disabled(true)

  def href(value: String): Property[Nothing] = attr("href", value)

  def id(value: String): Property[Nothing] = attr("id", value)

  def onClick[A](value: A): Property[A] = on("click", Action.Pure(value))

  def onSubmit[A](value: A): Property[A] = on("submit", Action.Pure(value))

  def style(declarations: Declarations): Property[Nothing] =
    attrs(
      "style",
      declarations.rows,
      Accumulator.Semicolon + Accumulator.Whitespace
    )

  def style(
      declaration: Declaration,
      declarations: Declaration*
  ): Property[Nothing] =
    style(Declarations.from(declaration +: declarations))
}
