package io.taig.schelm.dsl.util

trait Tagged[A] extends Any

object Tagged {
  type @@[+A, B] = A with Tagged[B]

  def apply[A, B](value: A): A @@ B = value.asInstanceOf[A @@ B]

  def of[A]: Tagger[A] = Tagger.asInstanceOf[Tagger[A]]

  sealed class Tagger[B] {
    def apply[A](value: A): A @@ B = value.asInstanceOf[A @@ B]
  }

  private object Tagger extends Tagger[Nothing]
}
