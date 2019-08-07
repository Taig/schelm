package io.taig.schelm

final case class Accumulator(value: String) extends AnyVal {
  def +(accumulator: Accumulator): Accumulator =
    Accumulator(value + accumulator.value)
}

object Accumulator {
  val Dash: Accumulator = Accumulator("-")

  val Comma: Accumulator = Accumulator(",")

  val Semicolon: Accumulator = Accumulator(";")

  val Whitespace: Accumulator = Accumulator(" ")
}
