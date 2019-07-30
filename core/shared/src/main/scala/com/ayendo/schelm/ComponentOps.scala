package com.ayendo.schelm

import cats.implicits._

abstract class ComponentOps[F[_], A](
    component: F[A],
    extract: F[A] => Component[F[A], A],
    inject: (Component[F[A], A], F[A]) => F[A]
) {
  final def setAttributes(attributes: Attributes[A]): F[A] =
    updateAttributes(_ => attributes)

  final def updateAttributes(f: Attributes[A] => Attributes[A]): F[A] =
    extract(component) match {
      case html: Component.Element[F[A], A] =>
        inject(html.copy(attributes = f(html.attributes)), component)
      case _ => component
    }

  final def setChildren(children: Children[F[A]]): F[A] =
    updateChildren(_ => children)

  final def updateChildren(f: Children[F[A]] => Children[F[A]]): F[A] =
    extract(component) match {
      case html: Component.Element[F[A], A] =>
        inject(html.copy(children = f(html.children)), component)
      case _ => component
    }
}
