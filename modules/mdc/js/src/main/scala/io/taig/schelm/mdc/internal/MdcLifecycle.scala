package io.taig.schelm.mdc.internal

import cats.effect.{IO, Resource}
import cats.implicits._
import io.taig.schelm.data.{BrowserLifecycle, Lifecycle}

import scala.scalajs.js

object MdcLifecycle {
  val chip: Lifecycle.Element = BrowserLifecycle.element { element =>
    Resource
      .make(IO(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCChip)(element))) { chip =>
        IO(chip.beginExit()).void
      }
      .void
  }

  val topAppBar: Lifecycle.Element = BrowserLifecycle.element { element =>
    Resource
      .liftF(IO(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCTopAppBar)(element)))
      .void
  }
}
