package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Functor, MonoidK, Traverse}

sealed abstract class Children[+A] extends Product with Serializable {
  final def indexed: List[A] = this match {
    case Children.Indexed(values)    => values
    case Children.Identified(values) => values.map { case (_, value) => value }
  }

  final def identified: List[(String, A)] = this match {
    case Children.Indexed(values)    => values.mapWithIndex((value, index) => (String.valueOf(index), value))
    case Children.Identified(values) => values
  }

  def ++[B >: A](children: Children[B]): Children[B] = (this, children) match {
    case (Children.Indexed(x), Children.Indexed(y))       => Children.Indexed(x ++ y)
    case (Children.Identified(x), Children.Identified(y)) => Children.Identified(x ++ y)
    case (x, y)                                           => Children.Identified(x.identified ++ y.identified)
  }
}

object Children {
  final case class Indexed[A](values: List[A]) extends Children[A]

  final case class Identified[A](values: List[(String, A)]) extends Children[A]

  val Empty: Children[Nothing] = empty[Nothing]

  def empty[A]: Children[A] = Indexed(List.empty[A])

  def from[A](children: Iterable[A]): Children[A] = Indexed(children.toList)

  def of[A](children: A*): Children[A] = from(children)

  implicit val monoidK: MonoidK[Children] = new MonoidK[Children] {
    override def empty[A]: Children[A] = Empty

    override def combineK[A](x: Children[A], y: Children[A]): Children[A] = x ++ y
  }

  implicit val functor: Functor[Children] = new Functor[Children] {
    override def map[A, B](fa: Children[A])(f: A => B): Children[B] = fa match {
      case Indexed(values)    => Indexed(values.map(f))
      case Identified(values) => Identified(values.map { case (key, child) => (key, f(child)) })
    }
  }

  implicit val traverse: Traverse[Children] = new Traverse[Children] {
    override def traverse[G[_]: Applicative, A, B](fa: Children[A])(f: A => G[B]): G[Children[B]] =
      fa match {
        case Indexed(values) => values.traverse(f).map(Indexed.apply)
        case Identified(values) =>
          values.traverse { case (key, child) => f(child).tupleLeft(key) }.map(Identified.apply)
      }

    override def foldLeft[A, B](fa: Children[A], b: B)(f: (B, A) => B): B =
      fa match {
        case Indexed(values)    => values.foldl(b)(f)
        case Identified(values) => values.foldl(b) { case (b, (_, value)) => f(b, value) }
      }

    override def foldRight[A, B](fa: Children[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case Indexed(values)    => values.foldr(lb)(f)
        case Identified(values) => values.foldr(lb) { case ((_, value), b) => f(value, b) }
      }
  }
}
