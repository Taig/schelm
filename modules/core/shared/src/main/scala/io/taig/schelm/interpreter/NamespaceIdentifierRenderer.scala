package io.taig.schelm.interpreter

import scala.annotation.tailrec

import cats.data.Kleisli
import cats.implicits._
import cats.{Eval, Functor, Monad, Traverse}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data._
import io.taig.schelm.implicits._
import io.taig.schelm.util.NodeAccessor

object NamespaceIdentifierRenderer {
  @tailrec
  private def unwrap[F[_], G[_[_], _]](
      namespace: Namespace[G[F, Fix[λ[A => Namespace[G[F, A]]]]]]
  )(implicit G: Functor[G[F, *]]): Fix[G[F, *]] = namespace match {
    case Namespace.Identified(_, namespace) => unwrap(namespace)
    case Namespace.Anonymous(value)         => unwrap(value)
  }

  private def unwrap[F[_], G[_[_], _]](value: G[F, Fix[λ[A => Namespace[G[F, A]]]]])(
      implicit G: Functor[G[F, *]]
  ): Fix[G[F, *]] = Fix(value.map(fix => unwrap[F, G](fix.unfix)))

  def apply[F[_]: Monad, G[_[_], _]: NodeAccessor](
      implicit G: Traverse[G[F, *]]
  ): Renderer[F, Fix[λ[A => Namespace[G[F, A]]]], IdentifierTree[Eval[Fix[G[F, *]]]]] = {
    val EmptyChildren: Map[Identifier, IdentifierTree[Eval[Fix[G[F, *]]]]] = IdentifierTree.EmptyChildren

    def render(namespace: Fix[λ[A => Namespace[G[F, A]]]]): F[Map[Identifier, IdentifierTree[Eval[Fix[G[F, *]]]]]] =
      namespace.unfix match {
        case Namespace.Identified(identifier, identified @ Namespace.Identified(_, inner)) =>
          render(Fix[λ[A => Namespace[G[F, A]]]](identified)).map { children =>
            Map(identifier -> IdentifierTree(Eval.later(unwrap(inner)), children))
          }
        case Namespace.Identified(identifier, anonymous: Namespace.Anonymous[G[F, Fix[λ[A => Namespace[G[F, A]]]]]]) =>
          flatten(anonymous.value).map(tree => Map(identifier -> tree))
        case Namespace.Anonymous(value) =>
          value.foldLeftM(EmptyChildren) {
            // TODO fail on key conflict (?)
            (children, namespace) => render(namespace).map(children ++ _)
          }
      }

    def flatten(g: G[F, Fix[λ[A => Namespace[G[F, A]]]]]): F[IdentifierTree[Eval[Fix[G[F, *]]]]] = g.children match {
      case Some(children) =>
        children.foldLeftM(IdentifierTree.leaf(Eval.later(unwrap(g)))) { (tree, child) =>
          render(child).map { children =>
            // TODO fail on key conflict (?)
            if (children.isEmpty) tree else tree.copy(children = tree.children ++ children)
          }
        }
      case None => IdentifierTree.leaf(Eval.later(unwrap(g))).pure[F]
    }

    Kleisli(namespace => render(namespace).map(IdentifierTree(Eval.later(unwrap(namespace.unfix)), _)))
  }
}
