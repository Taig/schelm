package io.taig.schelm.util

import cats.data.Ior

object Collections {
  def zipAll[A, B](left: Vector[A], right: Vector[B]): Vector[Ior[A, B]] = {
    val common = (left zip right).map((Ior.both[A, B] _).tupled)

    (left.length, right.length) match {
      case (x, y) if x == y => common
      case (x, y) if x < y  => common ++ right.slice(x, y).map(Ior.right[A, B])
      case (x, y) if x > y  => common ++ left.slice(y, x).map(Ior.left[A, B])
    }
  }
}
