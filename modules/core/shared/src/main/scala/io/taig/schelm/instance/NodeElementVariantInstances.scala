package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data.Node.Element.Variant.{Normal, Void}

trait NodeElementVariantInstances {
  implicit val nodeElementVariantTraverse: Traverse[Node.Element.Variant] = new Traverse[Node.Element.Variant] {
    override def traverse[G[_]: Applicative, A, B](fa: Variant[A])(f: A => G[B]): G[Variant[B]] = fa match {
      case Normal(children) => children.traverse(f).map(Normal[B])
      case Void             => Void.pure[G].widen
    }

    override def foldLeft[A, B](fa: Variant[A], b: B)(f: (B, A) => B): B = fa match {
      case Normal(children) => children.foldl(b)(f)
      case Void             => b
    }

    override def foldRight[A, B](fa: Variant[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case Normal(children) => children.foldr(lb)(f)
      case Void             => lb
    }
  }
}
