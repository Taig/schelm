package io.taig.schelm.dsl.syntax

import io.taig.schelm.css.data.CssWidget
import io.taig.schelm.data.Children
import io.taig.schelm.dsl.DslWidget
import io.taig.schelm.dsl.operation.ChildrenOperation

trait ChildrenSyntax[+F[_], -Context, +A] { self =>
//  def apply(children: DslWidget[F, Context]*): A =
//    self.children.set(Children.Indexed(children.toList))

  def children: ChildrenOperation[F, Context, A]
}
