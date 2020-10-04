package io.taig.schelm.mdc.internal

import cats.effect.{IO, Resource, Sync}
import cats.implicits._
import io.taig.schelm.data.Lifecycle
import org.scalajs.dom

import scala.scalajs.js

object MdcLifecycle {
  def chip[F[_]](implicit F: Sync[F]): Lifecycle.Element[F] = Lifecycle(
    mount = Some((element: dom.Element) =>
      Resource
        .make(F.delay(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCChip)(element))) { chip =>
          F.delay(chip.beginExit()).void
        }
        .void
    )
  )

  def topAppBar[F[_]](implicit F: Sync[F]): Lifecycle.Element[F] =
    Lifecycle(mount =
      Some((element: dom.Element) =>
        Resource
          .liftF(F.delay(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCTopAppBar)(element)))
          .void
      )
    )
}
