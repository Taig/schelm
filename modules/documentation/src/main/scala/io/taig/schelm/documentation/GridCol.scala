package io.taig.schelm.documentation

import io.taig.schelm.dsl.data.{DslNode, DslWidget}

final case class GridCol[Event, Context](node: DslNode.Element[Event, Context])
    extends DslWidget.Component[Event, Context] {
  override def render: DslWidget[Event, Context] =
    node
}
