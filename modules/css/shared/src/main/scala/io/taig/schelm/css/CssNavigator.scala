package io.taig.schelm.css

import io.taig.schelm.Navigator
import io.taig.schelm.css.data.Style
import io.taig.schelm.data.{Attributes, Children, Listeners, Widget}
import io.taig.schelm.data.Widget.{Patch, Pure, Render}

trait CssNavigator[A, B] extends Navigator[A, B] {
  def style(value: A, f: Style => Style): A
}

object CssNavigator {
  def apply[A, B](implicit navigator: CssNavigator[A, B]): CssNavigator[A, B] = navigator

//  implicit def widget[Event, Context, F[_], A](
//      implicit navigator: CssNavigator[Event, F[A], A]
//  ): CssNavigator[Event, Widget[Context, F[A]], A] = new CssNavigator[Event, Widget[Context, F[A]], A] {
//    override def attributes(widget: Widget[Context, F[A]], f: Attributes => Attributes): Widget[Context, F[A]] =
//      Navigator[Event, Widget[Context, F[A]], A].attributes(widget, f)
//
//    override def listeners(
//        widget: Widget[Context, F[A]],
//        f: Listeners[Event] => Listeners[Event]
//    ): Widget[Context, F[A]] =
//      Navigator[Event, Widget[Context, F[A]], A].listeners(widget, f)
//
//    override def children(widget: Widget[Context, F[A]], f: Children[A] => Children[A]): Widget[Context, F[A]] =
//      Navigator[Event, Widget[Context, F[A]], A].children(widget, f)
//
//    override def style(widget: Widget[Context, F[A]], f: Style => Style): Widget[Context, F[A]] =
//      widget match {
//        case widget: Patch[Context, F[A]]  => Patch(widget.f, style(widget.widget, f))
//        case widget: Pure[F[A]]            => Pure(navigator.style(widget.node, f))
//        case widget: Render[Context, F[A]] => Render(context => style(widget.f(context), f))
//      }
//  }
}
