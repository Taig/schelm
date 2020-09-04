package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.{Children, Node}
import io.taig.schelm.dsl.DslWidget
import io.taig.schelm.dsl.operation.ChildrenOperation

trait ChildrenSyntax[F[+_], Event, Context] { self =>
  final def apply(children: DslWidget[Node[Event, +*], Event, Context]*): DslWidget[F, Event, Context] =
    self.children.set(Children.Indexed(children.map(DslWidget.toStylesheetWidget[Event, Context]).toList))

  def children: ChildrenOperation[F, Event, Context]
}
