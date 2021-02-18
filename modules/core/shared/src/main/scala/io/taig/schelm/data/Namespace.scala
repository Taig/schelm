package io.taig.schelm.data

import io.taig.schelm.instance.NamespaceInstances

sealed abstract class Namespace[+A] extends Product with Serializable

object Namespace extends NamespaceInstances {
  final case class Anonymous[A](value: A) extends Namespace[A]
  final case class Identified[A](identifier: Identifier, namespace: Namespace[A]) extends Namespace[A]

  def apply[A](identifier: Identifier, namespace: Namespace[A]): Namespace[A] = Identified(identifier, namespace)

  def anonymous[A](value: A): Namespace[A] = Anonymous(value)

  def identified[A](identifier: Identifier, value: A): Namespace[A] = Identified(identifier, Anonymous(value))
}
