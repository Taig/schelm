package io.taig.schelm.data

import simulacrum.typeclass

object Has {
  @typeclass
  trait Element[In] {
    type Out

    def get(component: In): Component.Element[Out, Component.Element.Type[Out]]
  }
}
