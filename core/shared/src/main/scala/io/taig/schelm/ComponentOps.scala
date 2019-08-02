package io.taig.schelm

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

  final def children: Children[F[A]] =
    extract(component) match {
      case element: Component.Element[F[A], A]   => element.children
      case fragment: Component.Fragment[F[A], A] => fragment.children
      case _: Component.Text                     => Children.empty
    }

  final def setChildren(children: Children[F[A]]): F[A] =
    updateChildren(_ => children)

  final def updateChildren(f: Children[F[A]] => Children[F[A]]): F[A] =
    extract(component) match {
      case element: Component.Element[F[A], A] =>
        inject(element.copy(children = f(element.children)), component)
      case fragment: Component.Fragment[F[A], A] =>
        inject(fragment.copy(children = f(fragment.children)), component)
      case _ => component
    }

  final def setText(value: String): F[A] =
    extract(component) match {
      case text: Component.Text => inject(text.copy(value = value), component)
      case _                    => component
    }
}
