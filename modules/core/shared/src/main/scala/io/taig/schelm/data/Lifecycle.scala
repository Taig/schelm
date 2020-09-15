package io.taig.schelm.data

final case class Lifecycle[+A](mounted: A, unmount: A)

object Lifecycle {
  type Element[+Event] = Lifecycle[Callback.Element[Event]]

  object Element {
    val Empty: Lifecycle.Element[Nothing] = Lifecycle(Callback.Element.noop, Callback.Element.noop)
  }

  type Fragment[+Event] = Lifecycle[Callback.Fragment[Event]]

  object Fragment {
    val Empty: Lifecycle.Fragment[Nothing] = Lifecycle(Callback.Fragment.noop, Callback.Fragment.noop)
  }

  type Text[+Event] = Lifecycle[Callback.Text[Event]]

  object Text {
    val Empty: Lifecycle.Text[Nothing] = Lifecycle(Callback.Text.noop, Callback.Text.noop)
  }
}
