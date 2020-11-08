package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.Hydrater
import io.taig.schelm.data.{HtmlHydratedReference, HtmlReference, Listeners, Node, NodeReference}

object HtmlHydrater {
  def apply[F[_]](implicit F: MonadCancel[F, Throwable]): Hydrater[F, HtmlReference[F], HtmlHydratedReference[F]] = {
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
