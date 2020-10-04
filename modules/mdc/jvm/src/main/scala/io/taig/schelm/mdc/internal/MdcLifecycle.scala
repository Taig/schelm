package io.taig.schelm.mdc.internal

import io.taig.schelm.data.Lifecycle

object MdcLifecycle {
  def chip[F[_]]: Lifecycle.Element[F] = Lifecycle.Noop

  def topAppBar[F[_]]: Lifecycle.Element[F] = Lifecycle.Noop
}
