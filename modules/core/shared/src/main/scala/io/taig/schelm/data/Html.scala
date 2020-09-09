package io.taig.schelm.data

import io.taig.schelm.Navigator

final case class Html[+Event](component: Component[Event, Html[Event]]) extends AnyVal

object Html {
  implicit def navigator[Event]: Navigator[Event, Html[Event], Html[Event]] =
    new Navigator[Event, Html[Event], Html[Event]] {
      override def attributes(html: Html[Event], f: Attributes => Attributes): Html[Event] =
        Html(Navigator[Event, Component[Event, Html[Event]], Html[Event]].attributes(html.component, f))

      override def listeners(html: Html[Event], f: Listeners[Event] => Listeners[Event]): Html[Event] =
        Html(Navigator[Event, Component[Event, Html[Event]], Html[Event]].listeners(html.component, f))

      override def children(html: Html[Event], f: Children[Html[Event]] => Children[Html[Event]]): Html[Event] =
        Html(Navigator[Event, Component[Event, Html[Event]], Html[Event]].children(html.component, f))
    }
}
