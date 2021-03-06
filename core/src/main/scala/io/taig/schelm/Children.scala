package io.taig.schelm

import cats._
import cats.implicits._

import scala.collection.immutable.ListMap

sealed abstract class Children[A] extends Product with Serializable {
  final def isEmpty: Boolean = this match {
    case Children.Indexed(values)    => values.isEmpty
    case Children.Identified(values) => values.isEmpty
  }

  final def length: Int = this match {
    case Children.Indexed(values)    => values.length
    case Children.Identified(values) => values.size
  }

  def ++(children: Children[A]): Children[A] =
    (this, children) match {
      case (Children.Indexed(x), Children.Indexed(y)) =>
        Children.Indexed(x ++ y)
      case (Children.Identified(x), Children.Identified(y)) =>
        Children.Identified(x ++ y)
      case (Children.Indexed(x), Children.Identified(y)) =>
        Children.Identified(ListMap(x.zipWithIndex.map {
          case (value, index) => (String.valueOf(index), value)
        }: _*) ++ y)
      case (Children.Identified(x), Children.Indexed(y)) =>
        Children.Identified(x ++ ListMap(y.zipWithIndex.map {
          case (value, index) => (String.valueOf(index), value)
        }: _*))
    }

  def indexOf(key: Key): Option[Int] =
    (key, this) match {
      case (Key.Index(index), _) => index.some
      case (Key.Identifier(identifier), Children.Identified(values)) =>
        values.keysIterator.indexOf(identifier).some
      case (Key.Identifier(_), Children.Indexed(_)) => None
    }

  def map[B](f: (Key, A) => B): Children[B] =
    this match {
      case Children.Indexed(values) =>
        Children.indexed(values.zipWithIndex.map {
          case (value, index) => f(Key.Index(index), value)
        })
      case Children.Identified(values) =>
        Children.identified(values.map {
          case (key, value) => (key, f(Key.Identifier(key), value))
        })
    }

  def collect[B](f: PartialFunction[(Key, A), B]): Children[B] =
    this match {
      case Children.Indexed(values) =>
        Children.indexed(values.zipWithIndex.mapFilter {
          case (value, index) => f.lift((Key.Index(index), value))
        })
      case Children.Identified(values) =>
        Children.identified(values.toList.mapFilter {
          case (key, value) => f.lift((Key.Identifier(key), value)).tupleLeft(key)
        })
    }

  def append(key: Key, value: A): Children[A] =
    (key, this) match {
      case (_, Children.Indexed(values)) => Children.Indexed(values :+ value)
      case (Key.Identifier(identifier), Children.Identified(values)) =>
        Children.Identified(values.updated(identifier, value))
      case _ => this
    }

  def updated(key: Key, value: A): Children[A] =
    (key, this) match {
      case (Key.Index(index), Children.Indexed(values)) =>
        try Children.Indexed(values.updated(index, value))
        catch {
          case _: IndexOutOfBoundsException => this
        }
      case (Key.Identifier(identifier), Children.Identified(values)) =>
        Children.Identified(values.updated(identifier, value))
      case _ => this
    }

  def remove(key: Key): Children[A] =
    (key, this) match {
      case (Key.Index(index), Children.Indexed(values)) =>
        Children.Indexed(values.patch(index, Nil, 1))
      case (Key.Identifier(identifier), Children.Identified(values)) =>
        Children.Identified(values - identifier)
      case _ => this
    }

  def foldLeft[B](initial: B)(f: (B, Key, A) => B): B =
    toList.foldLeft(initial) {
      case (result, (key, value)) => f(result, key, value)
    }

  def traverse[G[_]: Applicative, B](f: (Key, A) => G[B]): G[Children[B]] =
    this match {
      case Children.Indexed(values) =>
        values.zipWithIndex
          .traverse { case (value, index) => f(Key.Index(index), value) }
          .map(Children.indexed)
      case Children.Identified(values) =>
        values.toList
          .traverse {
            case (key, value) => f(Key.Identifier(key), value).tupleLeft(key)
          }
          .map(Children.identified)
    }

  def traverse_[G[_]: Applicative, B](f: (Key, A) => G[B]): G[Unit] =
    this match {
      case Children.Indexed(values) =>
        values.zipWithIndex.traverse_ {
          case (value, index) => f(Key.Index(index), value)
        }
      case Children.Identified(values) =>
        values.toList.traverse_ {
          case (key, value) => f(Key.Identifier(key), value).tupleLeft(key)
        }
    }

  def flatTraverse[G[_]: Applicative, B](
      f: (Key, A) => G[List[B]]
  ): G[List[B]] =
    toList.flatTraverse { case (key, child) => f(key, child) }

  def get(key: Key): Option[A] = (key, this) match {
    case (Key.Index(index), Children.Indexed(values)) => values.lift(index)
    case (Key.Identifier(identifier), Children.Identified(values)) =>
      values.get(identifier)
    case _ => None
  }

  def toList: List[(Key, A)] = this match {
    case Children.Indexed(values) =>
      values.zipWithIndex.map {
        case (value, index) => Key.Index(index) -> value
      }
    case Children.Identified(values) =>
      values.toList.map {
        case (identifier, value) => Key.Identifier(identifier) -> value
      }
  }

  def keys: List[Key] = this match {
    case Children.Indexed(values) =>
      values.zipWithIndex.map { case (_, index) => Key.Index(index) }
    case Children.Identified(values) => values.keys.toList.map(Key.Identifier)
  }

  def values: List[A] = this match {
    case Children.Indexed(values)    => values
    case Children.Identified(values) => values.values.toList
  }
}

object Children {
  final case class Indexed[A](raw: List[A]) extends Children[A]

  final case class Identified[A](raw: ListMap[String, A]) extends Children[A]

  def empty[A]: Children[A] = Indexed(List.empty)

  def of[A](children: A*): Children[A] = indexed(children)

  def indexed[A](children: Iterable[A]): Children[A] = Indexed(children.toList)

  def identified[A](children: Iterable[(String, A)]): Children[A] =
    Identified(ListMap(children.toSeq: _*))
}
