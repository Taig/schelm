//package io.taig.schelm.dsl.syntax
//
//import io.taig.schelm.css.data.CssWidget
//import io.taig.schelm.dsl.internal.Has
//import io.taig.schelm.dsl.internal.Tagged.@@
//import io.taig.schelm.dsl.operation.{AttributesOperation, ChildrenOperation, CssOperation, ListenersOperation}
//
//final class NormalElementSyntax[Event, Context](widget: CssWidget[Event, Context] @@ NormalElementSyntax.Tag)
//    extends AttributesSyntax[Event, Context, NormalElementSyntax.Tag]
//    with ListenersSyntax[Event, Context, NormalElementSyntax.Tag]
//    with CssSyntax[Event, Context, NormalElementSyntax.Tag]
//    with ChildrenSyntax[Event, Context, NormalElementSyntax.Tag] {
//  override def listeners = new ListenersOperation[Event, Context, NormalElementSyntax.Tag](widget)
//
//  override def attributes = new AttributesOperation[Event, Context, NormalElementSyntax.Tag](widget)
//
//  override def css = new CssOperation[Event, Context, NormalElementSyntax.Tag](widget)
//
//  override def children = new ChildrenOperation[Event, Context, NormalElementSyntax.Tag](widget)
//}
//
//object NormalElementSyntax {
//  type Tag = Has.Attributes with Has.Listeners with Has.Css with Has.Children
//}
