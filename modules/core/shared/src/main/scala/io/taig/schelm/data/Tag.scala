package io.taig.schelm.data

import io.taig.schelm.Navigator

final case class Tag[+Event](name: String, attributes: Attributes, listeners: Listeners[Event])

object Tag {
  implicit def navigator[Event]: Navigator[Event, Tag[Event], Nothing] = new Navigator[Event, Tag[Event], Nothing] {
    override def attributes(tag: Tag[Event], f: Attributes => Attributes): Tag[Event] =
      tag.copy(attributes = f(tag.attributes))

    override def listeners(tag: Tag[Event], f: Listeners[Event] => Listeners[Event]): Tag[Event] =
      tag.copy(listeners = f(tag.listeners))

    override def children(tag: Tag[Event], f: Children[Nothing] => Children[Nothing]): Tag[Event] = tag
  }
}
