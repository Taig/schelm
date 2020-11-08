package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Hydrater}
import io.taig.schelm.data._
import io.taig.schelm.data.Node.Element.Variant
import alleycats.std.set._
import org.scalajs.dom.raw.Event

object HtmlHydrater {
  def apply[F[_]](dom: Dom[F], html: F[Html[F]])(
      implicit F: MonadCancel[F, Throwable]
  ): Hydrater[F, HtmlReference[F], (HtmlHydratedReference[F], ListenerTree[F])] = {
//    def notify(html: HtmlReference[F]): F[HtmlHydratedReference[F]] = html.reference match {
//      case reference: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
//        val element = reference.dom
//        val lifecycle = reference.node.lifecycle
//
//        html.reference.traverse(notify).flatMap { reference =>
//          lifecycle.mount.traverse(_.apply(element).allocated).map {
//            case Some((_, release)) => HtmlHydratedReference(???, release)
//            case None               => HtmlHydratedReference(???, F.unit)
//          }
//        }
//      case NodeReference.Fragment(Node.Fragment(_)) =>
//        html.reference.traverse(notify).map(_ => HtmlHydratedReference(???, F.unit))
//      case reference: NodeReference.Text[F, Listeners[F]] =>
//        reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
//          case Some((_, release)) => HtmlHydratedReference(???, release)
//          case None               => HtmlHydratedReference(???, F.unit)
//        }
//    }

    /** Create a listener that uses the `Path` and `Listener.Name` arguments to access the `Html` structure, find the
      * corresponding listener and triggers it
      */
    def listener(path: Path, name: Listener.Name, event: Event): F[Unit] = html.flatMap {
      Html.find(_)(path).flatMap(Html.listener(_)(name)) match {
        case Some(listener) => listener.apply(event)
        case None           => F.raiseError(new IllegalStateException(show"No listener for ${name.value} in $path"))
      }
    }

    def walk(html: HtmlReference[F], path: Path): F[HtmlHydratedReference[F]] = html.reference match {
      case reference: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
        val element = reference.dom
        val lifecycle = reference.node.lifecycle

        html.reference.node.traverseWithKey((key, child) => walk(child, path / key)).flatMap { node =>
          lifecycle.mount.traverse(_.apply(element).allocated).map {
            case Some((_, release)) => HtmlHydratedReference(???, release)
            case None               => HtmlHydratedReference(???, F.unit)
          }
        }

        val children = reference.node.variant match {
          case Variant.Normal(children) =>
            children.traverseWithKey((key, child) => walk(child, path / key)).map(_.toMap)
          case Variant.Void => ListenerTree.EmptyChildren.pure[F].widen
        }

//        children.map(ListenerTree(reference.node.tag.listeners, _)) <*
//          reference.node.tag.listeners.keys.traverse_ { name =>
//            val callback = dom.unsafeRun(listener(path, name, _))
//            dom.addEventListener(reference.dom, name.value, callback)
//          }
        ???
      case reference: NodeReference.Fragment[HtmlReference[F]] =>
//        reference.node.children
//          .traverseWithKey((key, child) => walk(child, path / key))
//          .map(_.toMap)
//          .map(ListenerTree(Listeners.Empty, _))
        ???
      case reference: NodeReference.Text[F, Listeners[F]] =>
//        ListenerTree(reference.node.listeners, ListenerTree.EmptyChildren).pure[F]
        ???
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
