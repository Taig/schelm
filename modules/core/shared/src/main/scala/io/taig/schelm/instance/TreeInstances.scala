package io.taig.schelm.instance

import cats.{Eq, Functor, Monoid}
import cats.syntax.all._
import io.taig.schelm.data.{Identifier, Tree}

trait TreeInstances {
  implicit def treeMonoid[A]: Monoid[Tree[A]] = new Monoid[Tree[A]] {
    override def empty: Tree[A] = Tree.Empty

    override def combine(x: Tree[A], y: Tree[A]): Tree[A] = x merge y
  }

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa.map(f)
  }

  implicit def treeEq[A: Eq]: Eq[Tree[A]] = new Eq[Tree[A]] {
    override def eqv(x: Tree[A], y: Tree[A]): Boolean =
      (x, y) match {
        case (Tree.Root(x), Tree.Root(y))           => Eq[Map[Identifier, Tree[A]]].eqv(x, y)
        case (Tree.Node(x1, x2), Tree.Node(y1, y2)) => x1 === y1 && Eq[Map[Identifier, Tree[A]]].eqv(x2, y2)
        case _                                      => false
      }
  }
}
