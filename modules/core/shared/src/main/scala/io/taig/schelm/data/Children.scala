package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Functor, MonoidK, Traverse}
import io.taig.schelm.util.Collections

import scala.collection.immutable.VectorMap

sealed abstract class Children[+A] extends Product with Serializable {
  final def get(key: Key): Option[A] = (this, key) match {
    case (Children.Indexed(children), Key.Index(index))              => children.lift(index)
    case (Children.Identified(children), Key.Identifier(identifier)) => children.get(identifier)
    case (_: Children.Indexed[_], _: Key.Identifier)                 => None
    case (_: Children.Identified[_], _: Key.Index)                   => None
  }

  final def indexed: Vector[A] = this match {
    case Children.Indexed(values)    => values
    case Children.Identified(values) => values.values.toVector
  }

  final def identified: VectorMap[String, A] = this match {
    case Children.Indexed(values)    => values.mapWithIndex((value, index) => (String.valueOf(index), value)).to(VectorMap)
    case Children.Identified(values) => values
  }

  final def updated[B >: A](index: Int, value: B): Children[B] = this match {
    case Children.Indexed(values) => Children.Indexed(values.updated(index, value))
    case Children.Identified(values) =>
      val key = values.keys.apply(index)
      Children.Identified(values.updated(key, value))
  }

  final def ++[B >: A](children: Children[B]): Children[B] = (this, children) match {
    case (Children.Indexed(x), Children.Indexed(y))       => Children.Indexed(x ++ y)
    case (Children.Identified(x), Children.Identified(y)) => Children.Identified(x ++ y)
    case (x, y)                                           => Children.Identified(x.identified ++ y.identified)
  }

  final def map[B](f: A => B): Children[B] = this match {
    case Children.Indexed(values)    => Children.Indexed(values.map(f))
    case Children.Identified(values) => Children.Identified(values.view.mapValues(f).to(VectorMap))
  }

  final def mapWithKey[B](f: (Key, A) => B): Children[B] = this match {
    case Children.Indexed(values) =>
      Children.Indexed(values.zipWithIndex.map { case (child, index) => f(Key.Index(index), child) })
    case Children.Identified(values) =>
      Children.Identified(values.map { case (key, child) => key -> f(Key.Identifier(key), child) })
  }

  final def traverse[G[_]: Applicative, B](f: A => G[B]): G[Children[B]] =
    this match {
      case Children.Indexed(values)    => values.traverse(f).map(Children.Indexed.apply)
      case Children.Identified(values) => Collections.traverseVectorMap(values)(f).map(Children.Identified.apply)
    }

  final def traverseWithKey[G[_]: Applicative, B](f: (Key, A) => G[B]): G[Children[B]] =
    this match {
      case Children.Indexed(values) =>
        values.zipWithIndex.traverse { case (child, index) => f(Key.Index(index), child) }.map(Children.Indexed.apply)
      case Children.Identified(values) =>
        Collections
          .traverseVectorMapWithKey(values)((identifier, value) => f(Key.Identifier(identifier), value))
          .map(Children.Identified.apply)
    }

  final def toMap: Map[Key, A] = this match {
    case Children.Indexed(values)    => values.zipWithIndex.map { case (child, index) => Key.Index(index) -> child }.toMap
    case Children.Identified(values) => values.map { case (key, child) => Key.Identifier(key) -> child }
  }
}

object Children {
  final case class Indexed[A](values: Vector[A]) extends Children[A]

  final case class Identified[A](values: VectorMap[String, A]) extends Children[A]

  val Empty: Children[Nothing] = empty[Nothing]

  def empty[A]: Children[A] = Indexed(Vector.empty[A])

  def from[A](children: Iterable[A]): Children[A] = Indexed(children.toVector)

  def fromOption[A](children: Option[A]): Children[A] = Indexed(children.toVector)

  def of[A](children: A*): Children[A] = from(children)

  implicit val monoidK: MonoidK[Children] = new MonoidK[Children] {
    override def empty[A]: Children[A] = Empty

    override def combineK[A](x: Children[A], y: Children[A]): Children[A] = x ++ y
  }

  implicit val functor: Functor[Children] = new Functor[Children] {
    override def map[A, B](fa: Children[A])(f: A => B): Children[B] = fa.map(f)
  }

  implicit val traverse: Traverse[Children] = new Traverse[Children] {
    override def traverse[G[_]: Applicative, A, B](fa: Children[A])(f: A => G[B]): G[Children[B]] = fa.traverse(f)

    override def foldLeft[A, B](fa: Children[A], b: B)(f: (B, A) => B): B = fa.indexed.foldl(b)(f)

    override def foldRight[A, B](fa: Children[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.indexed.foldr(lb)(f)
  }
}
