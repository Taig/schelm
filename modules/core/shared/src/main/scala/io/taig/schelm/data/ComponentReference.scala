package io.taig.schelm.data

import cats.Traverse
import io.taig.schelm.algebra.Dom

sealed abstract class ComponentReference[F[_], +A] extends Product with Serializable

object ComponentReference {
  final case class Element[F[_], A](component: Component.Element[F, A], dom: Dom.Element)
      extends ComponentReference[F, A]
  final case class Fragment[F[_], A](component: Component.Fragment[F, A]) extends ComponentReference[F, A]
  final case class Text[F[_]](component: Component.Text[F], dom: Dom.Text) extends ComponentReference[F, Nothing]

  implicit def traverse[F[_], Element, Text]: Traverse[ComponentReference[F, *]] = ???
}
