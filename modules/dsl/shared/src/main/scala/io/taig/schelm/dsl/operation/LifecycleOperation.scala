//package io.taig.schelm.dsl.operation
//
//import cats.implicits._
//import io.taig.schelm.css.data.CssWidget
//import io.taig.schelm.data.{Children, Component}
//import io.taig.schelm.dsl.internal.Tagged
//import io.taig.schelm.dsl.internal.Tagged.@@
//
//final class LifecycleOperation[F[_], Context, Tag](css: CssWidget[F, Context]) {
//  def set(children: Children[CssWidget[F, Context]]): CssWidget[F, Context] @@ Tag = patch(_ => children)
//
//  def patch(f: Children[CssWidget[F, Context]] => Children[CssWidget[F, Context]]): CssWidget[F, Context] @@ Tag =
//    Tagged(CssWidget(css.widget.map(_.map {
//      case component @ Component.Element(_, Component.Element.Type.Normal(children), _) =>
//        component.copy(tpe = Component.Element.Type.Normal(f(children)))
//      case component @ Component.Fragment(children, _) => component.copy(children = f(children))
//      case component                                   => component
//    })))
//}
