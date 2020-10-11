package io.taig.schelm.documentation

import io.taig.schelm.dsl.data.DslNode

final case class GridCol[F[_], Event, Context](node: DslNode.Element[F, Event, Context])
    extends DslNode.Component[F, Event, Context] {
  override def render: DslNode[F, Event, Context] = node
}
