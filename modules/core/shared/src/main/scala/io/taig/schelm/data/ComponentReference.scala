package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class ComponentReference[+Element, +Text, +A] extends Product with Serializable

object ComponentReference {
  final case class Element[Dom, A](component: Component.Element[A], dom: Dom)
      extends ComponentReference[Dom, Nothing, A]
  final case class Fragment[A](component: Component.Fragment[A]) extends ComponentReference[Nothing, Nothing, A]
  final case class Text[Dom](component: Component.Text, dom: Dom) extends ComponentReference[Nothing, Dom, Nothing]

  implicit def traverse[Element, Text]: Traverse[ComponentReference[Element, Text, *]] =
    new Traverse[ComponentReference[Element, Text, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: ComponentReference[Element, Text, A]
      )(f: A => G[B]): G[ComponentReference[Element, Text, B]] =
        fa match {
          case reference @ ComponentReference.Element(component, _) =>
            component.traverse(f).map(component => reference.copy(component = component))
          case reference @ ComponentReference.Fragment(component) =>
            component.traverse(f).map(component => reference.copy(component = component))
          case reference @ ComponentReference.Text(_, _) => reference.pure[G].widen
        }

      override def foldLeft[A, B](fa: ComponentReference[Element, Text, A], b: B)(f: (B, A) => B): B =
        fa match {
          case ComponentReference.Element(component, _) => component.foldl(b)(f)
          case ComponentReference.Fragment(component)   => component.foldl(b)(f)
          case ComponentReference.Text(component, _)    => component.foldl(b)(f)
        }

      override def foldRight[A, B](fa: ComponentReference[Element, Text, A], lb: Eval[B])(
          f: (A, Eval[B]) => Eval[B]
      ): Eval[B] =
        fa match {
          case ComponentReference.Element(component, _) => component.foldr(lb)(f)
          case ComponentReference.Fragment(component)   => component.foldr(lb)(f)
          case ComponentReference.Text(component, _)    => component.foldr(lb)(f)
        }
    }
}
