package io.taig.schelm.data

object Callback {
  abstract class Element[+Event] {
    def apply(platform: Platform)(element: platform.Element): Option[Event]
  }

  object Element {
    val noop: Callback.Element[Nothing] = new Callback.Element[Nothing] {
      override def apply(platform: Platform)(element: platform.Element): Option[Nothing] = None
    }
  }

  abstract class Fragment[+Event] {
    def apply(platform: Platform)(nodes: List[platform.Node]): Option[Event]
  }

  object Fragment {
    val noop: Callback.Fragment[Nothing] = new Callback.Fragment[Nothing] {
      override def apply(platform: Platform)(nodes: List[platform.Node]): Option[Nothing] = None
    }
  }

  abstract class Text[+Event] {
    def apply(platform: Platform)(text: platform.Text): Option[Event]
  }

  object Text {
    val noop: Callback.Text[Nothing] = new Callback.Text[Nothing] {
      override def apply(platform: Platform)(text: platform.Text): Option[Nothing] = None
    }
  }
}
