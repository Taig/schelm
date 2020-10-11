package io.taig.schelm.util

import cats.arrow.FunctionK
import cats.{Applicative, Id}

object FunctionKs {
  def liftId[F[_]](implicit F: Applicative[F]): FunctionK[Id, F] = λ[FunctionK[Id, F]](F.pure(_))
}
