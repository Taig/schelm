package io.taig.schelm.css

import io.taig.schelm.data.{Component, Widget}

package object data {
  type StyledWidget[Event, Context] = Widget[Event, Context, Stylesheet]

  type StyledComponent[Event] = Component[Event, Stylesheet]
}
