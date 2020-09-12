package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class Component[+F[_], +A] extends Product with Serializable

object Component {
  final case class Element[+F[_], +A](tag: Tag, tpe: Element.Type[A], lifecycle: Lifecycle[Callback.Element[F]])
      extends Component[F, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Type[+A] extends Product with Serializable

    object Type {
      final case class Normal[+A](children: Children[A]) extends Type[A]
      final case object Void extends Type[Nothing]

      implicit val traverse: Traverse[Type] = new Traverse[Type] {
        override def traverse[G[_]: Applicative, A, B](fa: Type[A])(f: A => G[B]): G[Type[B]] =
          fa match {
            case tpe: Normal[A] => tpe.children.traverse(f).map(Normal[B])
            case Void           => Void.pure[G].widen
          }

        override def foldLeft[A, B](fa: Type[A], b: B)(f: (B, A) => B): B = fa match {
          case tpe: Normal[A] => tpe.children.foldl(b)(f)
          case Void           => b
        }

        override def foldRight[A, B](fa: Type[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
          case tpe: Normal[A] => tpe.children.foldr(lb)(f)
          case Void           => lb
        }
      }
    }

    implicit def traverse[F[_]]: Traverse[Element[F, *]] = new Traverse[Element[F, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Element[F, A])(f: A => G[B]): G[Element[F, B]] =
        fa.tpe.traverse(f).map(tpe => fa.copy(tpe = tpe))

      override def foldLeft[A, B](fa: Element[F, A], b: B)(f: (B, A) => B): B = fa.tpe.foldl(b)(f)

      override def foldRight[A, B](fa: Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.tpe.foldr(lb)(f)
    }
  }

  final case class Fragment[+F[_], +A](children: Children[A], lifecycle: Lifecycle[Callback.Fragment[F]])
      extends Component[F, A]

  final case class Text[+F[_]](value: String, listeners: Listeners, lifecycle: Lifecycle[Callback.Text[F]])
      extends Component[F, Nothing]

  implicit def traverse[F[_]]: Traverse[Component[F, *]] = new Traverse[Component[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Component[F, A])(f: A => G[B]): G[Component[F, B]] =
      fa match {
        case component: Element[F, A] => component.traverse(f).widen
        case component: Fragment[F, A] =>
          component.children.traverse(f).map(children => component.copy(children = children))
        case component: Text[F] => component.pure[G].widen
      }

    override def foldLeft[A, B](fa: Component[F, A], b: B)(f: (B, A) => B): B =
      fa match {
        case component: Element[F, A]  => component.foldl(b)(f)
        case component: Fragment[F, A] => component.children.foldl(b)(f)
        case _: Text[F]                => b
      }

    override def foldRight[A, B](fa: Component[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case component: Element[F, A]  => component.foldr(lb)(f)
        case component: Fragment[F, A] => component.children.foldr(lb)(f)
        case _: Text[F]                => lb
      }
  }
}
