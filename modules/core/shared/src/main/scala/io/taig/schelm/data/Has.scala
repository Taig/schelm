package io.taig.schelm.data

object Has {
  trait Element[A] {
    def element[Event]: Component.Element[Event, A]
  }
}
