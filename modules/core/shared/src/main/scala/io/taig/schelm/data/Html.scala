package io.taig.schelm.data

final case class Html[Event](node: Node[Html[Event], Event]) extends AnyVal
