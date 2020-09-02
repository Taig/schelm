package io.taig.schelm.data

sealed abstract class Node[+A, +Event] extends Product with Serializable

/** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
object Element {
  final case class Normal[A, Event](tag: Tag[Event], children: Children[A]) extends Node[A, Event]

  final case class Void[Event](tag: Tag[Event]) extends Node[Nothing, Event]
}

final case class Fragment[A](children: Children[A]) extends Node[A, Nothing]

final case class Text[Event](value: String, listeners: Listeners[Event]) extends Node[Nothing, Event]
