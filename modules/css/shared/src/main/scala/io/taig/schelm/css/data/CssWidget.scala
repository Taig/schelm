package io.taig.schelm.css.data

import cats.implicits._
import io.taig.schelm.Navigator
import io.taig.schelm.css.CssNavigator
import io.taig.schelm.data.{Attributes, Children, Listeners, Widget}

final case class CssWidget[+F[_], -Context](
    widget: Widget[Context, CssNode[F, CssWidget[F, Context]]]
) extends AnyVal

object CssWidget {
  def toStylesheetHtml[F[_], Context](widget: CssWidget[F, Context], context: Context): CssHtml[F] =
    widget.widget match {
      case widget: Widget.Patch[Context, CssNode[F, CssWidget[F, Context]]] =>
        toStylesheetHtml(CssWidget(widget.widget), widget.f(context))
      case Widget.Pure(node) => CssHtml(node.map(toStylesheetHtml(_, context)))
      case Widget.Render(f)  => toStylesheetHtml(CssWidget(f(context)), context)
    }

//  implicit def navigator[Event, Context]: CssNavigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]] =
//    new CssNavigator[Event, CssWidget[Event, Context], CssWidget[Event, Context]] {
//      override def attributes(css: CssWidget[Event, Context], f: Attributes => Attributes): CssWidget[Event, Context] =
//        CssWidget(
//          Navigator[Event, Widget[Context, CssNode[Event, CssWidget[Event, Context]]], CssWidget[Event, Context]]
//            .attributes(css.widget, f)
//        )
//
//      override def listeners(
//          css: CssWidget[Event, Context],
//          f: Listeners[Event] => Listeners[Event]
//      ): CssWidget[Event, Context] =
//        CssWidget(
//          Navigator[Event, Widget[Context, CssNode[Event, CssWidget[Event, Context]]], CssWidget[Event, Context]]
//            .listeners(css.widget, f)
//        )
//
//      override def children(
//          css: CssWidget[Event, Context],
//          f: Children[CssWidget[Event, Context]] => Children[CssWidget[Event, Context]]
//      ): CssWidget[Event, Context] =
//        CssWidget(
//          Navigator[Event, Widget[Context, CssNode[Event, CssWidget[Event, Context]]], CssWidget[Event, Context]]
//            .children(css.widget, f)
//        )
//
//      override def style(css: CssWidget[Event, Context], f: Style => Style): CssWidget[Event, Context] =
//        CssWidget(
//          CssNavigator[Event, Widget[Context, CssNode[Event, CssWidget[Event, Context]]], CssWidget[Event, Context]]
//            .style(css.widget, f)
//        )
//    }
}
