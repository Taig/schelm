package io.taig.schelm.data

sealed abstract class Platform extends Product with Serializable

object Platform {
  final case object Js extends Platform
  final case object Jvm extends Platform
}
