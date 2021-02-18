package io.taig.schelm.dsl.syntax

import io.taig.schelm.dsl.LengthUnit

trait lengthUnit {
  implicit def lengthUnitFloatOps(value: Float): LengthUnit.Ops = new LengthUnit.Ops(value)

  implicit def lengthUnitIntOps(value: Int): LengthUnit.Ops = new LengthUnit.Ops(value.toFloat)
}

object lengthUnit extends lengthUnit
