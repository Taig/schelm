package io.taig.schelm.dsl.internal

sealed abstract class Has extends Product with Serializable

object Has {
  sealed trait Attributes extends Has
  sealed trait Children extends Has
  sealed trait Css extends Has
  sealed trait Listeners extends Has
}
