package io.taig.schelm.documentation

import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.{DslNode, DslWidget}

final case class GridRow[F[_], Context](
    node: DslNode.Element.Normal[F, Context],
    children: Children[GridCol[F, Context]]
) extends DslWidget.Component[F, Context] {
  override def render: DslWidget[F, Context] =
    node.copy(attributes = node.attributes + (a.cls := "row"), children = children)
}

object GridRow {
  def default[F[_], Context](children: Children[GridCol[F, Context]]): GridRow[F, Context] =
    GridRow(div(), children)
}
