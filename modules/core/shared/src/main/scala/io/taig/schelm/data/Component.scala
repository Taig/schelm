package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class Component[+A] extends Product with Serializable

object Component {
  final case class Element[+A](tag: Tag, tpe: Element.Type[A], lifecycle: Lifecycle[Callback.Element])
      extends Component[A]

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

    implicit val traverse: Traverse[Element] = new Traverse[Element] {
      override def traverse[G[_]: Applicative, A, B](fa: Element[A])(f: A => G[B]): G[Element[B]] =
        fa.tpe.traverse(f).map(tpe => fa.copy(tpe = tpe))

      override def foldLeft[A, B](fa: Element[A], b: B)(f: (B, A) => B): B = fa.tpe.foldl(b)(f)

      override def foldRight[A, B](fa: Element[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.tpe.foldr(lb)(f)
    }
  }

  final case class Fragment[+A](children: Children[A], lifecycle: Lifecycle[Callback.Fragment]) extends Component[A]

  object Fragment {
    implicit val traverse: Traverse[Fragment] = new Traverse[Fragment] {
      override def traverse[G[_]: Applicative, A, B](fa: Fragment[A])(f: A => G[B]): G[Fragment[B]] =
        fa.children.traverse(f).map(children => fa.copy(children = children))

      override def foldLeft[A, B](fa: Fragment[A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

      override def foldRight[A, B](fa: Fragment[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.children.foldr(lb)(f)
    }
  }

  final case class Text(value: String, listeners: Listeners, lifecycle: Lifecycle[Callback.Text])
      extends Component[Nothing]

  implicit val traverse: Traverse[Component] = new Traverse[Component] {
    override def traverse[G[_]: Applicative, A, B](fa: Component[A])(f: A => G[B]): G[Component[B]] =
      fa match {
        case component: Element[A]  => component.traverse(f).widen
        case component: Fragment[A] => component.traverse(f).widen
        case component: Text        => component.pure[G].widen
      }

    override def foldLeft[A, B](fa: Component[A], b: B)(f: (B, A) => B): B =
      fa match {
        case component: Element[A]  => component.foldl(b)(f)
        case component: Fragment[A] => component.foldl(b)(f)
        case _: Text                => b
      }

    override def foldRight[A, B](fa: Component[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case component: Element[A]  => component.foldr(lb)(f)
        case component: Fragment[A] => component.foldr(lb)(f)
        case _: Text                => lb
      }
  }
}
