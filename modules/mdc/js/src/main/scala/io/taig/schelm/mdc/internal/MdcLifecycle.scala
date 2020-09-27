package io.taig.schelm.mdc.internal

import cats.effect.{IO, Resource}
import cats.implicits._
import io.taig.schelm.data.Lifecycle
import org.scalajs.dom

import scala.scalajs.js

object MdcLifecycle {
  val chip: Lifecycle.Element = (element: dom.Element) =>
    Resource
      .make(IO(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCChip)(element))) { chip =>
        IO(chip.beginExit()).void
      }
      .void

  val topAppBar: Lifecycle.Element = (element: dom.Element) =>
    Resource
      .liftF(IO(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCTopAppBar)(element)))
      .void
}
