package io.taig.schelm.dsl.internal

object Tagged {
  def apply[T, U](t: T): T @@ U = t.asInstanceOf[T @@ U]

  trait Extra[+U]

  type @@[+T, U] = T with Extra[U]
}
