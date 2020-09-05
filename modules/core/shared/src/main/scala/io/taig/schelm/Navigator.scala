package io.taig.schelm

import io.taig.schelm.data.{Attributes, Children, Listeners}

trait Navigator[Event, A, B] {
  def attributes(value: A, f: Attributes => Attributes): A

  def listeners(value: A, f: Listeners[Event] => Listeners[Event]): A

  def children(value: A, f: Children[B] => Children[B]): A
}

object Navigator {
  def apply[Event, A, B](implicit navigator: Navigator[Event, A, B]): Navigator[Event, A, B] = navigator
}
