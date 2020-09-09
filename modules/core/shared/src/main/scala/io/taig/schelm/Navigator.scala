package io.taig.schelm

import io.taig.schelm.data.{Attributes, Children, Listeners}

trait Navigator[A, B] {
  def attributes(value: A, f: Attributes => Attributes): A

  def listeners(value: A, f: Listeners => Listeners): A

  def children(value: A, f: Children[B] => Children[B]): A
}

object Navigator {
  def apply[A, B](implicit navigator: Navigator[A, B]): Navigator[A, B] = navigator
}
