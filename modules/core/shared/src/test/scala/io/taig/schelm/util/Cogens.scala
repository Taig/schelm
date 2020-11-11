package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data.{Key, StateTree, StateTree}
import org.scalacheck.Cogen
import org.scalacheck.rng.Seed

object Cogens {
  val key: Cogen[Key] = Cogen { (seed: Seed, key: Key) =>
    key match {
      case Key.Identifier(value) => Cogen[String].perturb(seed, value)
      case Key.Index(value)      => Cogen[Int].perturb(seed.next, value)
    }
  }

  def stateTreeStates[A](payload: Cogen[A]): Cogen[StateTree.States[A]] =
    Cogen.it[Vector[A], A](_.iterator)(payload).contramap(_.values)

  def pathTreeChildren[A](payload: Cogen[A]): Cogen[Map[Key, StateTree[A]]] =
    Cogen.cogenMap(key, Ordering[Key], pathTree(payload))

  def pathTree[A](payload: Cogen[A]): Cogen[StateTree[A]] = Cogen { (seed: Seed, tree: StateTree[A]) =>
    payload.perturb(pathTreeChildren(payload).perturb(seed, tree.children), tree.value)
  }
}
