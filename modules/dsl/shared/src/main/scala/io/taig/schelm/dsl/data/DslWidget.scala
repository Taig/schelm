package io.taig.schelm.dsl.data

import cats.implicits._
import io.taig.schelm.css.data.{CssHtml, CssNode}
import io.taig.schelm.data.{Callback, Component, Lifecycle, Widget}
import io.taig.schelm.dsl.operation.{AttributesOperation, ChildrenOperation, LifecycleOperation}
import io.taig.schelm.dsl.syntax.{AttributesSyntax, ChildrenSyntax, LifecycleElementSyntax}

sealed abstract class DslWidget[-Context] extends Product with Serializable

object DslWidget {
  final case class Pure[Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]]) extends DslWidget[Context]

  abstract class Component[Context] extends DslWidget[Context] {
    def render: DslWidget[Context]
  }
}

//sealed abstract class DslWidget[-Context] extends Product with Serializable {
//  def widget: Widget[Context, CssNode[Component[DslWidget[Context]]]]
//}
//
//object DslWidget {
//  sealed abstract class Element[-Context]
//      extends DslWidget[Context]
//      with AttributesSyntax[DslWidget.Element, Context]
//      with LifecycleElementSyntax[DslWidget.Element, Context]
//
//  object Element {
//    final case class Normal[-Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]])
//        extends Element[Context]
//        with AttributesSyntax[DslWidget.Element.Normal, Context]
//        with LifecycleElementSyntax[DslWidget.Element.Normal, Context]
//        with ChildrenSyntax[DslWidget.Element.Normal, Context] {
//      override def attributes: AttributesOperation[DslWidget.Element.Normal, Context] =
//        new AttributesOperation[DslWidget.Element.Normal, Context](widget, Normal.apply)
//
//      override def lifecycle: LifecycleOperation[DslWidget.Element.Normal, Context, Callback.Element] =
//        new LifecycleOperation[Normal, Context, Callback.Element] {
//          override def patch(
//              f: Lifecycle[Callback.Element] => Lifecycle[Callback.Element]
//          ): DslWidget.Element.Normal[Context] =
//            DslWidget.Element.Normal(widget.map(_.map {
//              case component @ Component.Element(_, _, lifecycle) => component.copy(lifecycle = f(lifecycle))
//              case component                                      => component
//            }))
//        }
//
//      override def children: ChildrenOperation[DslWidget.Element.Normal, Context] =
//        new ChildrenOperation[DslWidget.Element.Normal, Context](widget) {
//          override def lift[A <: Context](
//              widget: Widget[A, CssNode[Component[DslWidget[A]]]]
//          ): DslWidget.Element.Normal[A] =
//            DslWidget.Element.Normal(widget)
//        }
//    }
//
//    final case class Void[-Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]])
//        extends Element[Context]
//        with AttributesSyntax[DslWidget.Element.Void, Context]
//        with LifecycleElementSyntax[DslWidget.Element.Void, Context] {
//      override def attributes: AttributesOperation[DslWidget.Element.Void, Context] =
//        new AttributesOperation[DslWidget.Element.Void, Context](widget, Void.apply)
//
//      override def lifecycle: LifecycleOperation[DslWidget.Element.Void, Context, Callback.Element] =
//        new LifecycleOperation[DslWidget.Element.Void, Context, Callback.Element] {
//          override def patch(
//              f: Lifecycle[Callback.Element] => Lifecycle[Callback.Element]
//          ): DslWidget.Element.Void[Context] =
//            DslWidget.Element.Void(widget.map(_.map {
//              case component @ Component.Element(_, _, lifecycle) => component.copy(lifecycle = f(lifecycle))
//              case component                                      => component
//            }))
//        }
//    }
//  }
//
//  final case class Fragment[-Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]])
//      extends DslWidget[Context]
//
//  final case class Text[-Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]])
//      extends DslWidget[Context]
//
//  def toCssHtml[Context](widget: Widget[Context, CssNode[Component[DslWidget[Context]]]], context: Context): CssHtml =
//    widget match {
//      case widget: Widget.Patch[Context, CssNode[Component[DslWidget[Context]]]] =>
//        toCssHtml(widget.widget, widget.f(context))
//      case Widget.Pure(component) => CssHtml(component.map(_.map(dsl => toCssHtml(dsl.widget, context))))
//      case Widget.Render(f)       => toCssHtml(f(context), context)
//    }
//}
