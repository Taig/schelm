package io.taig.schelm.mdc.internal

import io.taig.schelm.mdc.Component

abstract class MdcLifecycle[F[_], Element] {
  def initialize(component: Component, element: Element): F[Unit]
}
