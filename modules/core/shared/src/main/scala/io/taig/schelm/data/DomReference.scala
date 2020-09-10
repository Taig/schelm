//package io.taig.schelm.data
//
//import cats.{Applicative, Eval, Functor, Traverse}
//import cats.implicits._
//
//sealed abstract class DomReference[F[_], +Element, +Text, +A, +B] extends Product with Serializable
//
//object DomReference {
//  final case class Element[F[_], Dom, A <: Component.Element[B, Component.Element.Type[B]], B](component: F[A], dom: Dom) extends DomReference[F, Dom, Nothing, A, B]
//
//  final case class Fragment[F[_], B](component: F[Component.Fragment[B]]) extends DomReference[F, Nothing, Nothing, Component.Fragment[F[B]], B]
//
////  final case class Text[F[+_], Dom](component: F[Component.Text], dom: Dom) extends DomReference[F, Nothing, Dom, Nothing]
//
////  def toNodes[F[_]: Traverse, Node, Element <: Node, Text <: Node](reference: NodeReference[F, Element, Text]): List[Node] = reference match {
////    case NodeReference.Element(_, dom) => List(dom)
////    case NodeReference.Fragment(component) =>
////      component.map(_.children.indexed.flatTr)
////      ???
////    case NodeReference.Text(_, dom) => List(dom)
////  }
//
////  implicit def traverse[F[_]: Traverse, Element, Text, X]: Traverse[DomReference[F, Element, Text, X, *]] =
////    new Traverse[DomReference[F, Element, Text, X, *]] {
////      override def traverse[G[_]: Applicative, A, B](fa: DomReference[F, Element, Text, X, A])(f: A => G[B]): G[DomReference[F, Element, Text, X, B]] =
////        fa match {
////          case reference: DomReference.Element[F, Element, X, A] =>
////            reference.component.traverse {
////              case component: Component.Element[B, Component.Element.Type.Void.type] =>
////                DomReference.Element(component, reference.dom).pure[G]
////            }
////          case reference: DomReference.Fragment[F, A] =>
////            reference.component
////              .traverse(_.children.traverse(f))
////              .map(_.map(Component.Fragment[B]))
////              .map(DomReference.Fragment[F, B])
////        }
////
////      override def foldLeft[A, B](fa: DomReference[F, Element, Text, X, A], b: B)(f: (B, A) => B): B = ???
////
////      override def foldRight[A, B](fa: DomReference[F, Element, Text, X, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = ???
////    }
//
////  implicit def traverse[Element, Text]: Traverse[NodeReference[Element, Text, *]] =
////    new Traverse[NodeReference[Element, Text, *]] {
////      override def traverse[G[_]: Applicative, A, B](
////          fa: NodeReference[Element, Text, A]
////      )(f: A => G[B]): G[NodeReference[Element, Text, B]] =
////        fa match {
////          case reference: NodeReference.Element[Element, A] =>
////            reference.component.traverse(f).map(component => reference.copy(component = component))
////          case reference: NodeReference.Fragment[A] =>
////            reference.component.children
////              .traverse(f)
////              .map(children => NodeReference.Fragment(Component.Fragment(children)))
////          case reference: NodeReference.Text[Text] => reference.pure[G].widen
////        }
////
////      override def foldLeft[A, B](fa: NodeReference[Element, Text, A], b: B)(f: (B, A) => B): B =
////        fa match {
////          case reference: NodeReference.Element[Element, A] => reference.component.foldl(b)(f)
////          case reference: NodeReference.Fragment[A]         => reference.component.children.foldl(b)(f)
////          case _: NodeReference.Text[Text]          => b
////        }
////
////      override def foldRight[A, B](fa: NodeReference[Element, Text, A], lb: Eval[B])(
////          f: (A, Eval[B]) => Eval[B]
////      ): Eval[B] =
////        fa match {
////          case reference: NodeReference.Element[Element, A] => reference.component.foldr(lb)(f)
////          case reference: NodeReference.Fragment[A]         => reference.component.children.foldr(lb)(f)
////          case _: NodeReference.Text[Text]          => lb
////        }
////    }
//}