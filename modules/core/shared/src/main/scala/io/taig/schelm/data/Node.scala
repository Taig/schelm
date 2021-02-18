package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import io.taig.schelm.instance.{
  NodeElementInstances,
  NodeElementVariantInstances,
  NodeFragmentInstances,
  NodeInstances,
  NodePortalInstances,
  NodeWindowInstances
}
import io.taig.schelm.util.Text
import org.scalajs.dom.raw.{EventTarget, TextEvent}

sealed abstract class Node[+F[_], +A] extends Product with Serializable {
  override def toString: String = this match {
    case Node.Element(tag, Node.Element.Variant.Normal(Children.Empty), Lifecycle.Noop) =>
      tag.toString(tag => s"<$tag></$tag>")
    case Node.Element(tag, Node.Element.Variant.Normal(children), lifecycle) =>
      s"""Element(
         |  tag = ${Text.align(2)(tag.toString(tag => s"<$tag></$tag>"))},
         |  children = ${Text.align(2)(children.toString)},
         |  lifecycle = ${Text.align(2)(lifecycle.toString)}
         |)""".stripMargin
    case Node.Element(tag, Node.Element.Variant.Void, Lifecycle.Noop) => tag.toString(tag => s"<$tag />")
    case Node.Element(tag, Node.Element.Variant.Void, lifecycle) =>
      s"""Element(
         |  tag = ${Text.align(2)(tag.toString(tag => s"<$tag />"))},
         |  lifecycle = ${Text.align(2)(lifecycle.toString)}
         |)""".stripMargin
    case Node.Fragment(children, lifecycle) =>
      s"""Fragment(
         |  children = ${Text.align(2)(children.toString)},
         |  lifecycle = ${Text.align(2)(lifecycle.toString)}
         |)""".stripMargin
    case Node.Text(value, Listeners.Empty, Lifecycle.Noop) => s""""$value""""
    case Node.Text(value, listeners, lifecycle) =>
      s"""Text(
         |  value = "$value",
         |  listeners = ${Text.align(2)(listeners.toString)},
         |  lifecycle = ${Text.align(2)(lifecycle.toString)}
         |)""".stripMargin
  }
}

object Node extends NodeInstances {
  final case class Element[F[_], A](
      tag: Tag[F],
      variant: Element.Variant[A],
      lifecycle: Lifecycle[F, Dom.Element]
  ) extends Node[F, A]

  object Element extends NodeElementInstances {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Variant[+A] extends Product with Serializable

    object Variant extends NodeElementVariantInstances {
      final case class Normal[+A](children: Children[A]) extends Variant[A]
      final case object Void extends Variant[Nothing]
    }
  }

  final case class Fragment[F[_], A](
      children: Children[A],
      lifecycle: Lifecycle[F, Any]
  ) extends Node[F, A]

  object Fragment extends NodeFragmentInstances

  final case class Text[F[_]](
      value: String,
      listeners: Listeners[F],
      lifecycle: Lifecycle.Text[F]
  ) extends Node[F, Nothing]

  final case class Portal[F[_], A](target: F[Dom.Element], value: A) extends Node[F, A]

  object Portal extends NodePortalInstances

  final case class Environment[F[_], A](target: F[EventTarget], listeners: Listeners[F], value: A) extends Node[F, A]

  object Environment extends NodeWindowInstances
}
