package io.taig.schelm.instance

import cats._
import cats.syntax.all._
import io.taig.schelm.data.Children

trait ChildrenInstances {
  implicit val childrenTraverseMonadFunctorFilterMonoidK
      : Traverse[Children] with Monad[Children] with FunctorFilter[Children] with MonoidK[Children] =
    new Traverse[Children] with Monad[Children] with FunctorFilter[Children] with MonoidK[Children] {
      override def empty[A]: Children[A] = Children.Empty

      override def combineK[A](x: Children[A], y: Children[A]): Children[A] = x ++ y

      override val functor: Functor[Children] = this

      override def pure[A](x: A): Children[A] = Children.one(x)

      override def flatMap[A, B](fa: Children[A])(f: A => Children[B]): Children[B] =
        Children(fa.values.flatMap(f(_).values))

      override def tailRecM[A, B](a: A)(f: A => Children[Either[A, B]]): Children[B] =
        Children(Monad[Vector].tailRecM(a)(f(_).values))

      override def map[A, B](fa: Children[A])(f: A => B): Children[B] = fa.map(f)

      override def mapFilter[A, B](fa: Children[A])(f: A => Option[B]): Children[B] = Children(fa.values.mapFilter(f))

      override def traverse[G[_]: Applicative, A, B](fa: Children[A])(f: A => G[B]): G[Children[B]] =
        fa.values.traverse(f).map(Children[B])

      override def foldLeft[A, B](fa: Children[A], b: B)(f: (B, A) => B): B = fa.values.foldl(b)(f)

      override def foldRight[A, B](fa: Children[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.values.foldr(lb)(f)
    }

  implicit def childrenEq[A: Eq]: Eq[Children[A]] = Eq.by(_.values)
}
