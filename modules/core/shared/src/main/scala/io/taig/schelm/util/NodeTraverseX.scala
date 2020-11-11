//package io.taig.schelm.util
//
//import cats._
//import cats.implicits._
//import io.taig.schelm.data.Key
//import simulacrum.typeclass
//
//@typeclass
//trait NodeTraverseX[F[_]] extends Traverse[F] {
//  def traverseWithKey[G[_]: Applicative, A, B](fa: F[A])(f: (Key, A) => G[B]): G[F[B]]
//}
//
//object NodeTraverseX {
//  implicit def nested[F[_]: Traverse, G[_]](implicit G: NodeTraverseX[G]): NodeTraverseX[λ[A => F[G[A]]]] =
//    new NodeTraverseX[λ[A => F[G[A]]]] {
//      override def traverse[H[_]: Applicative, A, B](fa: F[G[A]])(f: A => H[B]): H[F[G[B]]] =
//        fa.traverse(G.traverse(_)(f))
//
//      override def foldLeft[A, B](fa: F[G[A]], b: B)(f: (B, A) => B): B =
//        fa.foldl(b)((b, ga) => G.foldLeft(ga, b)(f))
//
//      override def foldRight[A, B](fa: F[G[A]], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
//        fa.foldr(lb)((ga, b) => G.foldRight(ga, b)(f))
//
//      override def traverseWithKey[H[_]: Applicative, A, B](fa: F[G[A]])(f: (Key, A) => H[B]): H[F[G[B]]] =
//        fa.traverse(G.traverseWithKey(_)(f))
//    }
//}
