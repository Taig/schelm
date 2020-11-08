package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.{Hydrater, ListenersManager}
import io.taig.schelm.data.{Children, HtmlHydratedReference, HtmlReference, Key, ListenerTree, Listeners, Node, NodeReference, Path, PathTree}
import alleycats.std.map._
import io.taig.schelm.data.Node.Element.Variant

object HtmlHydrater {
  def apply[F[_]](listeners: ListenersManager[F])(implicit F: MonadCancel[F, Throwable]): Hydrater[F, HtmlReference[F], (HtmlHydratedReference[F], ListenerTree[F])] = {
    def notify(html: HtmlReference[F]): F[HtmlHydratedReference[F]] = html.reference match {
      case reference: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
        val element = reference.dom
        val lifecycle = reference.node.lifecycle

        html.reference.traverse(notify).flatMap { reference =>
          lifecycle.mount.traverse(_.apply(element).allocated).map {
            case Some((_, release)) => HtmlHydratedReference(???, release)
            case None               => HtmlHydratedReference(???, F.unit)
          }
        }
      case NodeReference.Fragment(Node.Fragment(_)) =>
        html.reference.traverse(notify).map(_ => HtmlHydratedReference(???, F.unit))
      case reference: NodeReference.Text[F, Listeners[F]] =>
        reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
          case Some((_, release)) => HtmlHydratedReference(???, release)
          case None               => HtmlHydratedReference(???, F.unit)
        }
    }

    def walk(html: HtmlReference[F]): PathTree[Listeners[F]] = {
      val (listeners, children) = html.reference.node match {
        case node: Node.Element[F, Listeners[F], HtmlReference[F]] =>
          val children = node.variant match {
            case Variant.Normal(children) => children.map(walk).toMap
            case Variant.Void => ListenerTree.EmptyChildren
          }

          (node.tag.listeners, children)
        case node: Node.Fragment[HtmlReference[F]] => (Listeners.Empty, node.children.map(walk).toMap)
        case node: Node.Text[F, Listeners[F]] => (node.listeners, ListenerTree.EmptyChildren)
      }

      ListenerTree(listeners, children)
    }

    Kleisli { html =>
      html.reference match {
        case reference: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
          reference.node
          ???
        case reference: NodeReference.Fragment[HtmlReference[F]] =>
          ???
        case reference: NodeReference.Text[F, Listeners[F]] =>
          ???
      }
    }
  }
}
