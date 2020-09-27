package io.taig.schelm.documentation

import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.{DslNode, DslWidget}

final case class GridRow[Event, Context](
    node: DslNode.Element.Normal[Event, Context],
    children: Children[GridCol[Event, Context]]
) extends DslWidget.Component[Event, Context] {
  override def render: DslWidget[Event, Context] =
    node.copy(attributes = node.attributes + (a.cls := "row"), children = children)
}

object GridRow {
  def default[Event, Context](children: Children[GridCol[Event, Context]]): GridRow[Event, Context] =
    GridRow(div(), children)
}
