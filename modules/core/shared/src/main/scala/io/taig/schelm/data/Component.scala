package io.taig.schelm.data

final case class Component[Event, Payload](node: Node[Event, Component[Event, Payload]], payload: Payload)
