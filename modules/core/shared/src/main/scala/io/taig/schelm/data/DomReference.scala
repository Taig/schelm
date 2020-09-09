package io.taig.schelm.data

import cats.{Applicative, Eval, Functor, Traverse}
import cats.implicits._

sealed abstract class DomReference[F[_], +Element, +Text, A <: Component[F[A]]] extends Product with Serializable

object DomReference {
//  final case class Element[F[+_ <: Component.Element[A, Component.Element.Type[A]]], Dom, A <: Component.Element[A, Component.Element.Type[A]]](component: F[A], dom: Dom) extends DomReference[F, Dom, Nothing, A]

//  final case class Fragment[F[+_], +A](component: F[Component.Fragment[F[A]]]) extends DomReference[F, Nothing, Nothing, A]
//
//  final case class Text[F[+_], Dom](component: F[Component.Text], dom: Dom) extends DomReference[F, Nothing, Dom, Nothing]

//  def toNodes[F[_]: Traverse, Node, Element <: Node, Text <: Node](reference: NodeReference[F, Element, Text]): List[Node] = reference match {
//    case NodeReference.Element(_, dom) => List(dom)
//    case NodeReference.Fragment(component) =>
//      component.map(_.children.indexed.flatTr)
//      ???
//    case NodeReference.Text(_, dom) => List(dom)
//  }

  implicit def traverse[F[_], Element, Text]: Traverse[({ type G[A <: Component[F[A]]] = DomReference[F, Element, Text, A] })#G] =
    ???

//    new Traverse[DomReference[Element, Text, *]] {
//      override def traverse[G[_]: Applicative, A, B](fa: DomReference[Element, Text, A])(f: A => G[B]): G[DomReference[Element, Text, B]] =
//        fa match {
//          case reference: DomReference.Element[Element, A] =>
//            reference.evidence.get(reference.component)
//            ???
//        }
//
//      override def foldLeft[A, B](fa: DomReference[Element, Text, A], b: B)(f: (B, A) => B): B = ???
//
//      override def foldRight[A, B](fa: DomReference[Element, Text, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = ???
//    }

//  implicit def traverse[Element, Text]: Traverse[NodeReference[Element, Text, *]] =
//    new Traverse[NodeReference[Element, Text, *]] {
//      override def traverse[G[_]: Applicative, A, B](
//          fa: NodeReference[Element, Text, A]
//      )(f: A => G[B]): G[NodeReference[Element, Text, B]] =
//        fa match {
//          case reference: NodeReference.Element[Element, A] =>
//            reference.component.traverse(f).map(component => reference.copy(component = component))
//          case reference: NodeReference.Fragment[A] =>
//            reference.component.children
//              .traverse(f)
//              .map(children => NodeReference.Fragment(Component.Fragment(children)))
//          case reference: NodeReference.Text[Text] => reference.pure[G].widen
//        }
//
//      override def foldLeft[A, B](fa: NodeReference[Element, Text, A], b: B)(f: (B, A) => B): B =
//        fa match {
//          case reference: NodeReference.Element[Element, A] => reference.component.foldl(b)(f)
//          case reference: NodeReference.Fragment[A]         => reference.component.children.foldl(b)(f)
//          case _: NodeReference.Text[Text]          => b
//        }
//
//      override def foldRight[A, B](fa: NodeReference[Element, Text, A], lb: Eval[B])(
//          f: (A, Eval[B]) => Eval[B]
//      ): Eval[B] =
//        fa match {
//          case reference: NodeReference.Element[Element, A] => reference.component.foldr(lb)(f)
//          case reference: NodeReference.Fragment[A]         => reference.component.children.foldr(lb)(f)
//          case _: NodeReference.Text[Text]          => lb
//        }
//    }
}