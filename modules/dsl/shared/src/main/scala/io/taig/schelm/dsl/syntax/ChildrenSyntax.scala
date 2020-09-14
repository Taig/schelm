package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.Children
import io.taig.schelm.dsl.DslWidget
import io.taig.schelm.dsl.operation.ChildrenOperation

trait ChildrenSyntax[+F[-_], -Context] { self =>
  def apply[B <: Context](children: DslWidget[B]*): F[B] =
    self.children.set(Children.Indexed(children.toList))

  def children: ChildrenOperation[F, Context]
}
