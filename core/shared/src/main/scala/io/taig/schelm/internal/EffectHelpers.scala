package io.taig.schelm.internal

import cats._
import cats.implicits._

case object EffectHelpers {
  def fail[F[_]]: FailBuilder[F] = new FailBuilder[F]

  final class FailBuilder[F[_]] {
    def apply[A](
        message: String
    )(implicit F: ApplicativeError[F, Throwable]): F[A] =
      F.raiseError[A](new RuntimeException(message))
  }

  def get[F[_]]: GetBuilder[F] = new GetBuilder[F]

  final class GetBuilder[F[_]] {
    def apply[A](value: Option[A], message: => String)(
        implicit F: ApplicativeError[F, Throwable]
    ): F[A] = value.liftTo[F](new RuntimeException(message))
  }
}
