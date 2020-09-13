package io.taig.schelm.dsl

import io.taig.schelm.css.data.CssNode
import io.taig.schelm.data.{Component, Widget}
import io.taig.schelm.dsl.operation.AttributesOperation
import io.taig.schelm.dsl.syntax.AttributesSyntax

sealed abstract class DslWidget[+F[_], -Context] extends Product with Serializable {
  def widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]]
}

object DslWidget {
  sealed abstract class Element[+F[_], -Context]
      extends DslWidget[F, Context]
      with AttributesSyntax[F, Context, DslWidget.Element[F, Context]]

  object Element {
    final case class Normal[+F[_], -Context](widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]])
        extends Element[F, Context]
        with AttributesSyntax[F, Context, DslWidget.Element.Normal[F, Context]] {
      override def attributes: AttributesOperation[F, Context, DslWidget.Element.Normal[F, Context]] =
        new AttributesOperation[F, Context, DslWidget.Element.Normal[F, Context]](widget, Normal.apply)
    }

    final case class Void[+F[_], -Context](widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]])
        extends Element[F, Context]
        with AttributesSyntax[F, Context, DslWidget.Element.Void[F, Context]] {
      override def attributes: AttributesOperation[F, Context, Void[F, Context]] =
        new AttributesOperation[F, Context, DslWidget.Element.Void[F, Context]](widget, Void.apply)
    }
  }

  final case class Fragment[+F[_], -Context](widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]])
      extends DslWidget[F, Context]

  final case class Text[+F[_], -Context](widget: Widget[Context, CssNode[Component[F, DslWidget[F, Context]]]])
      extends DslWidget[F, Context]
}
