package io.taig.schelm.data

import cats._
import cats.implicits._
import io.taig.schelm.data.Path./

import scala.annotation.tailrec

final case class PathTree[+A](value: A, children: Map[Key, PathTree[A]]) { self =>
  @tailrec
  def find(path: Path): Option[PathTree[A]] = path match {
    case Path.Root => Some(this)
    case head / tail =>
      children.get(head) match {
        case Some(child) => child.find(tail)
        case None        => None
      }
  }

  @inline
  def get(key: Key): Option[PathTree[A]] = children.get(key)

  def updatedWith[B >: A](path: Path)(f: Option[PathTree[B]] => Option[PathTree[B]]): PathTree[B] = path match {
    case head / Path.Root => copy(children = children.updatedWith[PathTree[B]](head)(f))
    case head / tail =>
      children.get(head) match {
        case Some(child: PathTree[B]) => copy(children = children.updated(head, child.updatedWith[B](tail)(f)))
        case None                     => this
      }
    case Path.Root => f(Some(this)).getOrElse(copy(children = Map.empty))
  }

  def delete(path: Path): PathTree[A] = updatedWith(path)(_ => None)

  def merge[B >: A](tree: PathTree[B])(implicit monoid: Monoid[B]): PathTree[B] = {
    val selfChildrenKeys = self.children.keySet
    val treeChildrenKeys = tree.children.keySet

    val keysSelfOnly = selfChildrenKeys diff treeChildrenKeys
    val keysTreeOnly = treeChildrenKeys diff selfChildrenKeys
    val keysBoth = selfChildrenKeys intersect treeChildrenKeys

    val children = keysSelfOnly.map(key => key -> self.children(key)) ++
      keysTreeOnly.map(key => key -> tree.children(key)) ++
      keysBoth.map(key => key -> self.children(key).merge(tree.children(key)))

    PathTree(monoid.combine(value, tree.value), children.toMap)
  }
}

object PathTree {
  def empty[A: Monoid]: PathTree[A] = PathTree(value = Monoid[A].empty, children = Map.empty)

  implicit def monoid[A: Monoid]: Monoid[PathTree[A]] = new Monoid[PathTree[A]] {
    override val empty: PathTree[A] = PathTree.empty[A]

    override def combine(x: PathTree[A], y: PathTree[A]): PathTree[A] = x merge y
  }

  implicit def eq[A: Eq]: Eq[PathTree[A]] = new Eq[PathTree[A]] {
    override def eqv(x: PathTree[A], y: PathTree[A]): Boolean =
      x.value === y.value && x.children === y.children
  }
}
