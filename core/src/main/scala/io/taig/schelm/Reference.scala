package io.taig.schelm

import io.taig.schelm.{Element => SElement, Node => SNode, Text => SText}

sealed abstract class Reference[+A] extends Product with Serializable {
  final def root: List[SNode] = this match {
    case Reference.Element(_, node) => List(node)
    case Reference.Fragment(component) =>
      component.children.values.flatMap(_.root)
    case Reference.Text(_, node) => List(node)
  }
}

object Reference {
  final case class Element[A](
      component: Component.Element[Reference[A], A],
      node: SElement
  ) extends Reference[A]

  final case class Fragment[A](
      component: Component.Fragment[Reference[A]]
  ) extends Reference[A]

  final case class Text(component: Component.Text, node: SText)
      extends Reference[Nothing]

  def extract[A](reference: Reference[A]): Component[Reference[A], A] =
    reference match {
      case Reference.Element(component, _) => component
      case Reference.Fragment(component)   => component
      case Reference.Text(component, _)    => component
    }

  def inject[A](
      component: Component[Reference[A], A],
      reference: Reference[A]
  ): Reference[A] = {
    (component, reference) match {
      case (component: Component.Element[Reference[A], A], Element(_, node)) =>
        Element(component, node)
      case (component: Component.Fragment[Reference[A]], Fragment(_)) =>
        Fragment(component)
      case (component: Component.Text, Text(_, node)) => Text(component, node)
      case _                                          => reference
    }
  }

  implicit class ReferenceSyntax[A](reference: Reference[A])
      extends ComponentOps[Reference, A](
        reference,
        extract[A],
        inject[A]
      )
}
