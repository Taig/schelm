package io.taig.schelm.util

import cats.Applicative
import cats.implicits._

import scala.collection.immutable.VectorMap

object Collections {
  def traverseVectorMap[F[_]: Applicative, A, B, C](fa: VectorMap[A, B])(f: B => F[C]): F[VectorMap[A, C]] =
    fa.toVector.traverse { case (key, value) => f(value).tupleLeft(key) }.map(_.to(VectorMap))

  def traverseVectorMapWithKey[F[_]: Applicative, A, B, C](fa: VectorMap[A, B])(f: (A, B) => F[C]): F[VectorMap[A, C]] =
    fa.toVector.traverse { case (key, value) => f(key, value).tupleLeft(key) }.map(_.to(VectorMap))
}
