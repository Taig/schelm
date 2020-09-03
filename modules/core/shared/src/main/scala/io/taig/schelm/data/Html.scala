package io.taig.schelm.data

final case class Html[+Event](node: Node[Event, Html[Event]]) extends AnyVal
