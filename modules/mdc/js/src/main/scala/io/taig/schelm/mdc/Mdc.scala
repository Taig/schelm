package io.taig.schelm.mdc

import cats.effect.IO
import cats.implicits._
import io.taig.schelm.data.{BrowserCallback, Callback}

import scala.scalajs.js

object Mdc {
  val chip: Callback.Element[Nothing] = Callback.Element.noop
//    BrowserCallback.element { element =>
//    IO(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCChip)(element)).void
//  }
}
