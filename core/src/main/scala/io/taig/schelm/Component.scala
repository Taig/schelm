package io.taig.schelm

import cats._

sealed abstract class Component[+A, +Event] extends Product with Serializable

object Component {
  final case class Element[A, Event](
      name: String,
      namespace: Option[String],
      attributes: Attributes,
      listeners: Listeners[Event],
      children: Children[A]
  ) extends Component[A, Event]

  final case class Fragment[A](children: Children[A])
      extends Component[A, Nothing]

  final case class Lazy[A](eval: Eval[A], hash: Int)
      extends Component[A, Nothing]

  final case class Text(value: String) extends Component[Nothing, Nothing]

  implicit def functor[Event]: Functor[Component[?, Event]] =
    new Functor[Component[?, Event]] {
      override def map[A, B](
          fa: Component[A, Event]
      )(f: A => B): Component[B, Event] =
        fa match {
          case Element(name, namespace, attributes, listeners, children) =>
            Element(
              name,
              namespace,
              attributes,
              listeners,
              children.map((_, value) => f(value))
            )
          case Fragment(children) =>
            Fragment(children.map((_, value) => f(value)))
          case Lazy(component, hash) =>
            Lazy(component.map(f), hash)
          case component: Text => component
        }
    }
}
