package io.taig.schelm.dsl.syntax

import io.taig.schelm.algebra.Dom
import io.taig.schelm.data.Callback
import io.taig.schelm.dsl.operation.LifecycleOperation

trait LifecycleElementSyntax[+F[-_], -Context] { self =>
  def mounted(callback: Callback.Element): F[Context] =
    self.lifecycle.patch(lifecycle => lifecycle.copy(mounted = Some(callback)))

  def unmount(callback: Callback.Element): F[Context] =
    self.lifecycle.patch(lifecycle => lifecycle.copy(unmount = Some(callback)))

  def lifecycle: LifecycleOperation[F, Context, Callback.Element]
}
