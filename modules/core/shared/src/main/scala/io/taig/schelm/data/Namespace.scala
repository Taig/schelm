package io.taig.schelm.data

sealed abstract class Namespace[+A] extends Product with Serializable

object Namespace {
  final case class Identified[A](identifier: Identifier, namespace: Namespace[A]) extends Namespace[A]
  final case class Anonymous[A](value: A) extends Namespace[A]
}
