package io.taig.schelm

import scala.annotation.tailrec

abstract class ComponentOps[F[_], A](
    component: F[A],
    extract: F[A] => Component[F[A], A],
    inject: (Component[F[A], A], F[A]) => F[A]
) {
  final def setAttributes(attributes: Attributes): F[A] =
    updateAttributes(_ => attributes)

  final def updateAttributes(f: Attributes => Attributes): F[A] =
    ComponentOps.updateAttributes(component, extract, inject)(f)

  final def setListeners(listeners: Listeners[A]): F[A] =
    updateListeners(_ => listeners)

  final def updateListeners(f: Listeners[A] => Listeners[A]): F[A] =
    ComponentOps.updateListeners(component, extract, inject)(f)

  final def children: Children[F[A]] =
    ComponentOps.children(component, extract, inject)

  final def setChildren(children: Children[F[A]]): F[A] =
    updateChildren(_ => children)

  final def updateChildren(f: Children[F[A]] => Children[F[A]]): F[A] =
    ComponentOps.updateChildren(component, extract, inject)(f)

  final def setText(value: String): F[A] = updateText(_ => value)

  final def updateText(f: String => String): F[A] =
    ComponentOps.updateText(component, extract, inject)(f)
}

object ComponentOps {
  @tailrec
  final def updateAttributes[F[_], A](
      component: F[A],
      extract: F[A] => Component[F[A], A],
      inject: (Component[F[A], A], F[A]) => F[A]
  )(f: Attributes => Attributes): F[A] =
    extract(component) match {
      case element: Component.Element[F[A], A] =>
        inject(element.copy(attributes = f(element.attributes)), component)
      case lzy: Component.Lazy[F[A]] =>
        updateAttributes(lzy.eval.value, extract, inject)(f)
      case _: Component.Fragment[F[A]] => component
      case _: Component.Text           => component
    }

  @tailrec
  final def updateListeners[F[_], A](
      component: F[A],
      extract: F[A] => Component[F[A], A],
      inject: (Component[F[A], A], F[A]) => F[A]
  )(f: Listeners[A] => Listeners[A]): F[A] =
    extract(component) match {
      case element: Component.Element[F[A], A] =>
        inject(element.copy(listeners = f(element.listeners)), component)
      case lzy: Component.Lazy[F[A]] =>
        updateListeners(lzy.eval.value, extract, inject)(f)
      case _: Component.Fragment[F[A]] => component
      case _: Component.Text           => component
    }

  @tailrec
  final def children[F[_], A](
      component: F[A],
      extract: F[A] => Component[F[A], A],
      inject: (Component[F[A], A], F[A]) => F[A]
  ): Children[F[A]] =
    extract(component) match {
      case component: Component.Element[F[A], A] => component.children
      case component: Component.Fragment[F[A]]   => component.children
      case component: Component.Lazy[F[A]] =>
        children(component.eval.value, extract, inject)
      case _: Component.Text => Children.empty
    }

  @tailrec
  final def updateChildren[F[_], A](
      component: F[A],
      extract: F[A] => Component[F[A], A],
      inject: (Component[F[A], A], F[A]) => F[A]
  )(f: Children[F[A]] => Children[F[A]]): F[A] =
    extract(component) match {
      case element: Component.Element[F[A], A] =>
        inject(element.copy(children = f(element.children)), component)
      case fragment: Component.Fragment[F[A]] =>
        inject(fragment.copy(children = f(fragment.children)), component)
      case lzy: Component.Lazy[F[A]] =>
        updateChildren(lzy.eval.value, extract, inject)(f)
      case _: Component.Text => component
    }

  @tailrec
  final def updateText[F[_], A](
      component: F[A],
      extract: F[A] => Component[F[A], A],
      inject: (Component[F[A], A], F[A]) => F[A]
  )(f: String => String): F[A] =
    extract(component) match {
      case text: Component.Text =>
        inject(text.copy(value = f(text.value)), component)
      case lzy: Component.Lazy[F[A]] =>
        updateText(lzy.eval.value, extract, inject)(f)
      case _: Component.Element[F[A], A] => component
      case _: Component.Fragment[F[A]]   => component
    }
}
