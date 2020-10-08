package io.taig.schelm.util

import cats.{Applicative, Id}
import cats.arrow.FunctionK

object FunctionKs {
  def liftId[F[_]](implicit F: Applicative[F]): FunctionK[Id, F] = λ[FunctionK[Id, F]](F.pure(_))
}
