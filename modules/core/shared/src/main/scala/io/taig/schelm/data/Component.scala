package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.Navigator

sealed abstract class Component[F[_], +A] extends Product with Serializable

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

      implicit def navigator[A]: Navigator[Type[A], A] = new Navigator[Type[A], A] {
        override def attributes(tpe: Type[A], f: Attributes => Attributes): Type[A] = tpe

        override def listeners(tpe: Type[A], f: Listeners => Listeners): Type[A] = tpe

        override def children(tpe: Type[A], f: Children[A] => Children[A]): Type[A] =
          tpe match {
            case Normal(children) => Normal(f(children))
            case Void             => Void
          }
      }
    }

//    implicit val traverse: Traverse[Element[*]] = new Traverse[Element[*]] {
//      override def traverse[G[_]: Applicative, A, B](fa: Element[A])(f: A => G[B]): G[Element[B]] =
//        fa.tpe.traverse(f).map(tpe => fa.copy(tpe = tpe))
//
//      override def foldLeft[A, B](fa: Element[A], b: B)(f: (B, A) => B): B = fa.tpe.foldl(b)(f)
//
//      override def foldRight[A, B](fa: Element[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
//        fa.tpe.foldr(lb)(f)
//    }
//
//    implicit def has[A]: Has.Element.Aux[Element[A], A] = Has.Element.instance(identity)
//
//    implicit def navigator[A, B]: Navigator[Element[B], B] =
//      new Navigator[Element[B], B] {
//        override def attributes(element: Element[B], f: Attributes => Attributes): Element[B] =
//          element.copy(tag = Navigator[Tag, Nothing].attributes(element.tag, f))
//
//        override def listeners(element: Element[B], f: Listeners => Listeners): Element[B] =
//          element.copy(tag = Navigator[Tag, Nothing].listeners(element.tag, f))
//
//        override def children(element: Element[B], f: Children[B] => Children[B]): Element[B] =
//          element.copy(tpe = Navigator[Type[B], B].children(element.tpe, f))
//      }
  }

  final case class Fragment[+F[_], +A](children: Children[A], lifecycle: Lifecycle[Callback.Fragment[F]])
      extends Component[F, A]

  final case class Text[+F[_]](value: String, listeners: Listeners, lifecycle: Lifecycle[Callback.Text[F]])
      extends Component[F, Nothing]

  implicit def traverse[F[_]]: Traverse[Component[F, *]] = ???
//    new Traverse[Component[*]] {
//    override def traverse[G[_]: Applicative, A, B](
//        fa: Component[A]
//    )(f: A => G[B]): G[Component[B]] =
//      fa match {
//        case node: Element[A] => node.traverse(f).widen
//        case node: Fragment[A]       => node.children.traverse(f).map(children => node.copy(children = children))
//        case node: Text       => node.pure[G].widen
//      }
//
//    override def foldLeft[A, B](fa: Component[A], b: B)(f: (B, A) => B): B =
//      fa match {
//        case node: Element[A] => node.foldl(b)(f)
//        case node: Fragment[A]       => node.children.foldl(b)(f)
//        case _: Text          => b
//      }
//
//    override def foldRight[A, B](fa: Component[A], lb: Eval[B])(
//        f: (A, Eval[B]) => Eval[B]
//    ): Eval[B] =
//      fa match {
//        case node: Element[A] => node.foldr(lb)(f)
//        case node: Fragment[A]       => node.children.foldr(lb)(f)
//        case _: Text          => lb
//      }
//  }

//  implicit def navigator[A]: Navigator[Component[A], A] =
//    new Navigator[Component[A], A] {
//      override def attributes(
//          node: Component[A],
//          f: Attributes => Attributes
//      ): Component[A] =
//        node match {
//          case node: Element[A]         => Navigator[Element[A], A].attributes(node, f)
//          case _: Fragment[A] | _: Text => node
//        }
//
//      override def listeners(node: Component[A], f: Listeners => Listeners): Component[A] =
//        node match {
//          case node: Element[A] => Navigator[Element[A], A].listeners(node, f)
//          case node: Text       => node.copy(listeners = f(node.listeners))
//          case _: Fragment[A]          => node
//        }
//
//      override def children(
//          node: Component[A],
//          f: Children[A] => Children[A]
//      ): Component[A] =
//        node match {
//          case node: Element[A] => Navigator[Element[A], A].children(node, f)
//          case node: Text       => node
//          case node: Fragment[A]       => node.copy(children = f(node.children))
//        }
//    }
}
