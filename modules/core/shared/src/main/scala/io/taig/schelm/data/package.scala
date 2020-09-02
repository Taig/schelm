package io.taig.schelm

package object data {
  type Html[+Event] = Component[Event, Unit, Unit]
}
