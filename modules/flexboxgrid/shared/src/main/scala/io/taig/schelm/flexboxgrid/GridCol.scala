package io.taig.schelm.flexboxgrid

import io.taig.schelm.data.Attributes
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.DslNode

final case class GridCol[+F[_], +Event, -Context](f: Attributes => DslNode[F, Event, Context])
    extends DslNode.Element.Normal[F, Event, Context] {
  override def render: DslNode[F, Event, Context] =
    f(Attributes.of(a.cls := "col-xs-12"))
}

object GridCol {}
