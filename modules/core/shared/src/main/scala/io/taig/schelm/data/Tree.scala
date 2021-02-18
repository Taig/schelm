package io.taig.schelm.data

import scala.annotation.tailrec

import cats.Monoid
import cats.syntax.all._
import io.taig.schelm.data.Identification./
import io.taig.schelm.instance.TreeInstances

sealed abstract class Tree[+A] extends Product with Serializable {
  def get(identifier: Identifier): Option[Tree[A]]

  def payload: Option[A]

  @tailrec
  final def find(identification: Identification): Option[Tree[A]] = identification match {
    case Identification.Root => Some(this)
    case head / tail =>
      get(head) match {
        case Some(child) => child.find(tail)
        case None        => None
      }
  }

  def merge[B >: A](tree: Tree[B]): Tree[B] = deepMergeWith[B](tree, (_, right) => right)

  /** Recursively merge `this` `Tree` with the given `tree` */
  def deepMerge[B >: A](tree: Tree[B])(implicit monoid: Monoid[B]): Tree[B] = deepMergeWith(tree, monoid.combine)

  //  def delete(identification: Identification): Tree[A] = identification match {
  //    case head / Identification.Root => copy(children = children.removed(head))
  //    case head / tail                => get(head).fold(this)(child => updated(head, child.delete(tail)))
  //    case Identification.Root        => copy(children = Map.empty)
  //  }
  //
  //  @inline
  //  def updated[B >: A](identifier: Identifier, tree: Tree[B]): Tree[B] =
  //    copy(children = children.updated(identifier, tree))
  //
  def deepMergeWith[B >: A](tree: Tree[B], combine: (B, B) => B): Tree[B] = ??? // {
  //    val keys = children.keySet ++ tree.children.keySet
  //
  //    val builder = Map.newBuilder[Identifier, Tree[B]]
  //
  //    keys.foreach { key =>
  //      (get(key), tree.get(key)) match {
  //        case (Some(x), Some(y)) => builder.addOne(key -> x.merge(y, combine))
  //        case (Some(x), None)    => builder.addOne(key -> x)
  //        case (None, Some(y))    => builder.addOne(key -> y)
  //        case (None, None)       => ()
  //      }
  //    }
  //
  //    Tree(combine(value, tree.value), builder.result())
  //  }

  def map[B](f: A => B): Tree[B]
}

object Tree extends TreeInstances {
  final case class Root[A](children: Map[Identifier, Node[A]]) extends Tree[A] {
    override def get(identifier: Identifier): Option[Tree[A]] = children.get(identifier)

    override def payload: Option[A] = None

    override def map[B](f: A => B): Root[B] = Root(children.fmap(_.map(f)))
  }

  final case class Node[A](value: A, children: Map[Identifier, Node[A]]) extends Tree[A] {
    override def get(identifier: Identifier): Option[Tree[A]] = children.get(identifier)

    override def payload: Option[A] = Some(value)

    override def map[B](f: A => B): Node[B] = Node(f(value), children.fmap(_.map(f)))
  }

  val Empty: Tree[Nothing] = Tree.Root(Map.empty)

  def of[A](children: (Identifier, Node[A])*): Tree[A] = Tree.Root(children.toMap)

  def leaf[A](value: A): Tree[A] = Tree.Node(value, Map.empty)
}
