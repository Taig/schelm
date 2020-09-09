//package io.taig.schelm.data
//
//import scala.annotation.unchecked.uncheckedVariance
//
//import cats.{Applicative, Eval, Traverse}
//import cats.implicits._
//
//final case class Lifecycle[+F[_], A, +B](
//    value: B,
//    mounted: A => F[Unit]
////    unmount: A => F[Unit]
//)
//
//object Lifecycle {
//  implicit def traverse[F[_], A]: Traverse[Lifecycle[F, A, *]] = new Traverse[Lifecycle[F, A, *]] {
//    override def traverse[G[_]: Applicative, B, C](fa: Lifecycle[F, A, B])(f: B => G[C]): G[Lifecycle[F, A, C]] =
//      f(fa.value).map(value => fa.copy(value = value))
//
//    override def foldLeft[B, C](fa: Lifecycle[F, A, B], b: C)(f: (C, B) => C): C = f(b, fa.value)
//
//    override def foldRight[B, C](fa: Lifecycle[F, A, B], lb: Eval[C])(f: (B, Eval[C]) => Eval[C]): Eval[C] =
//      f(fa.value, lb)
//  }
//}
