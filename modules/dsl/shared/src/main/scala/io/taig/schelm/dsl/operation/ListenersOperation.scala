//package io.taig.schelm.dsl.operation
//
//import io.taig.schelm.Navigator
//import io.taig.schelm.css.data.CssWidget
//import io.taig.schelm.data.Listeners
//import io.taig.schelm.dsl.internal.Tagged
//import io.taig.schelm.dsl.internal.Tagged.@@
//
//final class ListenersOperation[Event, Context, Tag](widget: CssWidget[Event, Context]) {
//  def set(listeners: Listeners[Event]): CssWidget[Event, Context] @@ Tag = patch(_ => listeners)
//
//  def patch(f: Listeners[Event] => Listeners[Event]): CssWidget[Event, Context] @@ Tag =
//    Tagged(Navigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]].listeners(widget, f))
//}
