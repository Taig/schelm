package io.taig.schelm.internal

trait Platform {
  type Node

  type Element <: Node

  type Text <: Node

  type Document <: Node

  type Listener
}
