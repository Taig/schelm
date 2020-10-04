package io.taig.schelm.documentation

import io.taig.schelm.dsl.data.{DslNode, DslWidget}

final case class GridCol[F[_], Context](node: DslNode.Element[F, Context]) extends DslWidget.Component[F, Context] {
  override def render: DslWidget[F, Context] = node
}
