package io.taig.schelm.data

import io.taig.schelm.instance.ContextualInstances

sealed abstract class Contextual[-A, +B] extends Product with Serializable {
  def provide(context: A): B
}

object Contextual extends ContextualInstances {
  final case class Local[A, B](context: A => A, render: A => B) extends Contextual[A, B] {
    override def provide(value: A): B = render(context(value))
  }

  final case class Pure[A](value: A) extends Contextual[Any, A] {
    override def provide(context: Any): A = value
  }

  def pure[A](value: A): Contextual[Any, A] = Pure(value)

  def local[A, B](context: A => A)(render: A => B): Contextual[A, B] = Local(context, render)

  def use[A, B](render: A => B): Contextual[A, B] = Local(identity[A], render)
}
