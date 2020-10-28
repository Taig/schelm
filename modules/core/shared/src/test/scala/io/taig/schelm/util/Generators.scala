package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data.{Key, StateTree}
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object Generators {
  val identifier: Gen[String] = Gen.choose(1, 20).flatMap(Gen.listOfN(_, Gen.alphaNumChar)).map(_.mkString)

  val key: Gen[Key] = Gen.oneOf(identifier.map(Key.Identifier), Gen.posNum[Int].map(Key.Index))

  def stateTreeChildren[A](payload: Gen[A], maxDepth: Int): Gen[Map[Key, StateTree[A]]] =
    if (maxDepth == 0) Gen.const(Map.empty[Key, StateTree[A]])
    else
      Gen.choose(0, 3).flatMap { size =>
        Gen
          .listOfN(size, key)
          .map(_.distinct)
          .flatMap(_.traverse(key => stateTree(payload, maxDepth - 1).tupleLeft(key)))
          .map(_.toMap)
      }

  def stateTree[A](payload: Gen[A], maxDepth: Int): Gen[StateTree[A]] =
    for {
      values <- Gen.listOf(payload).map(_.toVector)
      children <- stateTreeChildren(payload, maxDepth)
    } yield StateTree(values, children)
}
