package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data
import io.taig.schelm.data.{Key, Listener, Listeners, StateTree, StateTree}
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object Generators {
  val identifier: Gen[String] = Gen.choose(1, 20).flatMap(Gen.listOfN(_, Gen.alphaNumChar)).map(_.mkString)

  val key: Gen[Key] = Gen.oneOf(identifier.map(Key.Identifier), Gen.posNum[Int].map(Key.Index))

  def listener[F[_]](action: Gen[Listener.Action[F]]): Gen[Listener[F]] =
    (identifier.map(Listener.Name.apply), action).mapN(Listener.apply)

  def listeners[F[_]](action: Gen[Listener.Action[F]]): Gen[Listeners[F]] =
    Gen.listOf(listener(action)).map(Listeners.from)

  def stateTreeStates[A](payload: Gen[A], maxLength: Int): Gen[StateTree.States[A]] =
    Gen.choose(0, maxLength).flatMap(Gen.listOfN(_, payload).map(_.toVector)).map(StateTree.States.apply)

  def pathTreeChildren[A](payload: Gen[A], maxDepth: Int): Gen[Map[Key, StateTree[A]]] =
    if (maxDepth == 0) Gen.const(Map.empty[Key, StateTree[A]])
    else
      Gen.choose(0, 3).flatMap { size =>
        Gen
          .listOfN(size, key)
          .map(_.distinct)
          .flatMap(_.traverse(key => pathTree(payload, maxDepth - 1).tupleLeft(key)))
          .map(_.toMap)
      }

  def pathTree[A](payload: Gen[A], maxDepth: Int): Gen[StateTree[A]] =
    for {
      value <- payload
      children <- pathTreeChildren(payload, maxDepth)
    } yield data.StateTree(value, children)
}
