package io.taig.schelm.mdc.internal

import cats.Applicative
import io.taig.schelm.mdc.Component

object JvmMdcLifecycle {
  def apply[F[_], Element](implicit F: Applicative[F]): MdcLifecycle[F, Element] = new MdcLifecycle[F, Element] {
    override def initialize(component: Component, element: Element): F[Unit] = F.unit
  }
}
