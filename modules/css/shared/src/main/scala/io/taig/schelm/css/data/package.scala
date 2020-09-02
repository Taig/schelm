package io.taig.schelm.css

import io.taig.schelm.data.{Component, Component}

package object data {
  type StyledWidget[Event, Context] = Component[Event, Context, Stylesheet]

  type StyledComponent[Event] = Component[Event, Stylesheet]
}
