package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data.{Key, StateTree}
import org.scalacheck.Cogen
import org.scalacheck.rng.Seed

object Cogens {
  val key: Cogen[Key] = Cogen { (seed: Seed, key: Key) =>
    key match {
      case Key.Identifier(value) => Cogen[String].perturb(seed, value)
      case Key.Index(value)      => Cogen[Int].perturb(seed.next, value)
    }
  }

  def vector[A](value: Cogen[A]): Cogen[Vector[A]] = Cogen.cogenList[A](value).contramap[Vector[A]](_.toList)

  def stateTreeChildren[A](payload: Cogen[A]): Cogen[Map[Key, StateTree[A]]] =
    Cogen.cogenMap(key, Ordering[Key], stateTree(payload))

  def stateTree[A](payload: Cogen[A]): Cogen[StateTree[A]] = Cogen { (seed: Seed, tree: StateTree[A]) =>
    vector(payload).perturb(stateTreeChildren(payload).perturb(seed, tree.children), tree.values)
  }
}
