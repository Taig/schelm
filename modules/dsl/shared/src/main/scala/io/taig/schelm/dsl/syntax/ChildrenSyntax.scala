//package io.taig.schelm.dsl.syntax
//
//import io.taig.schelm.css.data.CssWidget
//import io.taig.schelm.data.Children
//import io.taig.schelm.dsl.internal.Tagged.@@
//import io.taig.schelm.dsl.operation.ChildrenOperation
//
//trait ChildrenSyntax[Event, Context, Tag] { self =>
//  final def apply(children: CssWidget[Event, Context]*): CssWidget[Event, Context] @@ Tag =
//    self.children.set(Children.Indexed(children.toList))
//
//  def children: ChildrenOperation[Event, Context, Tag]
//}
