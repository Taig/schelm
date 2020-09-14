package io.taig.schelm.dsl.operation

import io.taig.schelm.data.Lifecycle

abstract class LifecycleOperation[+F[-_], -Context, A] {
  def patch(f: Lifecycle[A] => Lifecycle[A]): F[Context]
}
