package io.taig.schelm.util

import cats.implicits._
import io.taig.schelm.data.{Identifier, IdentifierTree, Key, StateTree}
import org.scalacheck.Cogen
import org.scalacheck.rng.Seed

object Cogens {
  val key: Cogen[Key] = Cogen { (seed: Seed, key: Key) =>
    key match {
      case Key.Identifier(value) => Cogen[String].perturb(seed, value)
      case Key.Index(value)      => Cogen[Int].perturb(seed.next, value)
    }
  }

  val identifier: Cogen[Identifier] = Cogen[String].contramap(_.value)

  def identifierTreeChildren[A](payload: Cogen[A]): Cogen[Map[Identifier, IdentifierTree[A]]] =
    Cogen.cogenMap(identifier, Ordering[Identifier], identifierTree(payload))

  def identifierTree[A](payload: Cogen[A]): Cogen[IdentifierTree[A]] = Cogen { (seed: Seed, tree: IdentifierTree[A]) =>
    payload.perturb(identifierTreeChildren(payload).perturb(seed, tree.children), tree.value)
  }
}
