package io.taig.schelm.documentation

import io.taig.schelm.dsl.data.{DslNode, DslWidget}

final case class GridCol[F[_], Event, Context](node: DslNode.Element[F, Event, Context])
    extends DslWidget.Component[F, Event, Context] {
  override def render: DslWidget[F, Event, Context] = node
}
