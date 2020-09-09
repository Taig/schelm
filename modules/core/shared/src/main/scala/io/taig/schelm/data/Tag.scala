package io.taig.schelm.data

import io.taig.schelm.Navigator

final case class Tag(name: String, attributes: Attributes, listeners: Listeners)

object Tag {
  implicit val navigator: Navigator[Tag, Nothing] = new Navigator[Tag, Nothing] {
    override def attributes(tag: Tag, f: Attributes => Attributes): Tag = tag.copy(attributes = f(tag.attributes))

    override def listeners(tag: Tag, f: Listeners => Listeners): Tag = tag.copy(listeners = f(tag.listeners))

    override def children(tag: Tag, f: Children[Nothing] => Children[Nothing]): Tag = tag
  }
}
