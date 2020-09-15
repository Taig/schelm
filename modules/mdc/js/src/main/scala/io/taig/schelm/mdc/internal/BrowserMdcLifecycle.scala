package io.taig.schelm.mdc.internal

import cats.effect.Sync
import io.taig.schelm.mdc.Component
import org.scalajs.dom

import scala.scalajs.js

object BrowserMdcLifecycle {
  def apply[F[_]](implicit F: Sync[F]): MdcLifecycle[F, dom.Element] = new MdcLifecycle[F, dom.Element] {
    override def initialize(component: Component, element: dom.Element): F[Unit] = component match {
      case Component.Chip =>
        F.delay(js.Dynamic.newInstance(js.Dynamic.global.mdc.chips.MDCChip)(element))
      case Component.ChipSet => F.unit
    }
  }
}
