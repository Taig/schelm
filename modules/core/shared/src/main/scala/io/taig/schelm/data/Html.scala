package io.taig.schelm.data

import io.taig.schelm.Navigator

final case class Html[+Event](node: Node[Event, Html[Event]]) extends AnyVal

object Html {
  implicit def navigator[Event]: Navigator[Event, Html[Event], Html[Event]] =
    new Navigator[Event, Html[Event], Html[Event]] {
      override def attributes(html: Html[Event], f: Attributes => Attributes): Html[Event] =
        Html(Navigator[Event, Node[Event, Html[Event]], Html[Event]].attributes(html.node, f))

      override def listeners(html: Html[Event], f: Listeners[Event] => Listeners[Event]): Html[Event] =
        Html(Navigator[Event, Node[Event, Html[Event]], Html[Event]].listeners(html.node, f))

      override def children(html: Html[Event], f: Children[Html[Event]] => Children[Html[Event]]): Html[Event] =
        Html(Navigator[Event, Node[Event, Html[Event]], Html[Event]].children(html.node, f))
    }
}
