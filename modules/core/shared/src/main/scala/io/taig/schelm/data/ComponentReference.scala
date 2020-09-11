package io.taig.schelm.data

import cats.Traverse

sealed abstract class ComponentReference[F[_], +Element, +Text, +A] extends Product with Serializable

object ComponentReference {
  final case class Element[F[_], Dom, A](component: Component.Element[F, A], dom: Dom)
      extends ComponentReference[F, Dom, Nothing, A]
  final case class Fragment[F[_], A](component: Component.Fragment[F, A])
      extends ComponentReference[F, Nothing, Nothing, A]
  final case class Text[F[_], Dom](component: Component.Text[F], dom: Dom)
      extends ComponentReference[F, Nothing, Dom, Nothing]

  implicit def traverse[F[_], Element, Text]: Traverse[ComponentReference[F, Element, Text, *]] = ???
}
