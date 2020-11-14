package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data._
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object Generators {
  val identifier: Gen[String] = Gen.choose(1, 20).flatMap(Gen.listOfN(_, Gen.alphaNumChar)).map(_.mkString)

  val key: Gen[Key] = Gen.oneOf(identifier.map(Key.Identifier), Gen.posNum[Int].map(Key.Index))

  def listener[F[_]](action: Gen[Listener.Action[F]]): Gen[Listener[F]] =
    (identifier.map(Listener.Name.apply), action).mapN(Listener.apply)

  def listeners[F[_]](action: Gen[Listener.Action[F]]): Gen[Listeners[F]] =
    Gen.listOf(listener(action)).map(Listeners.from)

  def identifierTreeChildren[A](payload: Gen[A], maxDepth: Int): Gen[Map[Identifier, IdentifierTree[A]]] =
    if (maxDepth == 0) Gen.const(IdentifierTree.EmptyChildren)
    else
      Gen.choose(0, 3).flatMap { size =>
        Gen
          .listOfN(size, identifier.map(Identifier.apply))
          .map(_.distinct)
          .flatMap(_.traverse(key => identifierTree(payload, maxDepth - 1).tupleLeft(key)))
          .map(_.toMap)
      }

  def identifierTree[A](payload: Gen[A], maxDepth: Int): Gen[IdentifierTree[A]] =
    for {
      value <- payload
      children <- identifierTreeChildren(payload, maxDepth)
    } yield IdentifierTree(value, children)
}
