package io.taig.schelm.data

import cats._
import cats.implicits._

final case class Children[+A](values: Vector[A]) extends AnyVal {
  def ++[B >: A](children: Children[B]): Children[B] = Children(values ++ children.values)
}

object Children {
  val Empty: Children[Nothing] = empty[Nothing]

  def empty[A]: Children[A] = Children(Vector.empty[A])

  def from[A](children: Iterable[A]): Children[A] = Children(children.toVector)

  def fromOption[A](children: Option[A]): Children[A] = Children(children.toVector)

  def of[A](children: A*): Children[A] = from(children)

  implicit val monoidK: MonoidK[Children] = new MonoidK[Children] {
    override def empty[A]: Children[A] = Empty

    override def combineK[A](x: Children[A], y: Children[A]): Children[A] = x ++ y
  }

  implicit val traverse: Traverse[Children] with FunctorFilter[Children] =
    new Traverse[Children] with FunctorFilter[Children] {
      override val functor: Functor[Children] = this

      override def mapFilter[A, B](fa: Children[A])(f: A => Option[B]): Children[B] = Children(fa.values.mapFilter(f))

      override def traverse[G[_]: Applicative, A, B](fa: Children[A])(f: A => G[B]): G[Children[B]] =
        fa.values.traverse(f).map(Children[B])

      override def foldLeft[A, B](fa: Children[A], b: B)(f: (B, A) => B): B = fa.values.foldl(b)(f)

      override def foldRight[A, B](fa: Children[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.values.foldr(lb)(f)
    }
}
