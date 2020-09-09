package io.taig.schelm

package object data {
  type Pure[+A] = A

  object Pure {
    @inline
    def apply[A](value: A): Pure[A] = value.asInstanceOf[Pure[A]]
  }
}
