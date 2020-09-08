package io.taig.schelm.internal

import io.taig.schelm.data.Platform

trait DomPlatform {
  type Node

  type Element <: Node

  type Text <: Node

  type Listener

  def platform: Platform = throw new IllegalStateException("DomPlatform must be replaced by platform specific override")
}
