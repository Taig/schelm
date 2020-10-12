package io.taig.schelm.flexboxgrid

import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode

final case class GridCol[+F[_], +Event, -Context](node: DslNode.Element[F, Event, Context])
    extends DslNode.Component[F, Event, Context] {
  override def render: DslNode[F, Event, Context] =
    node.copy(attributes = node.attributes + (a.cls := "col-xs-12"))
}
