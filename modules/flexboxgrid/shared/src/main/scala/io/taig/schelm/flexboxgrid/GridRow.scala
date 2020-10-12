package io.taig.schelm.flexboxgrid

import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode

final case class GridRow[+F[_], +Event, -Context](
    node: DslNode.Element.Normal[F, Event, Context],
    children: Children[GridCol[F, Event, Context]]
) extends DslNode.Component[F, Event, Context] {
  override def render: DslNode[F, Event, Context] =
    node.copy(attributes = node.attributes + (a.cls := "row"), children = children)
}

object GridRow {
  def default[F[_], Event, Context](children: Children[GridCol[F, Event, Context]]): GridRow[F, Event, Context] =
    GridRow(div(), children)
}
