package io.taig.schelm.data

trait Platform {
  type Node

  type Element <: Node

  type Text <: Node

  type Document <: Node

  type Listener

  def isJs: Boolean

  def isJvm: Boolean
}
