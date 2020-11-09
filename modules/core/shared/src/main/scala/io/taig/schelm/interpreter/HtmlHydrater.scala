package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Hydrater}
import io.taig.schelm.data._
import io.taig.schelm.data.Node.Element.Variant
import alleycats.std.set._
import io.taig.schelm.util.PathTraversal
import org.scalajs.dom.raw.Event

object HtmlHydrater {
  def apply[F[_]](dom: Dom[F], html: F[Html[F]])(
      implicit F: MonadCancel[F, Throwable]
  ): Hydrater[F, HtmlReference[F], HtmlHydratedReference[F]] = {

    /** Create a listener that uses the `Path` and `Listener.Name` arguments to access the `Html` structure, find the
      * corresponding listener and triggers it
      */
    def listener(path: Path, name: Listener.Name, event: Event): F[Unit] = html.flatMap { html =>
      PathTraversal[Html, F].find(html)(path).flatMap(PathTraversal[Html, F].listener(_)(name)) match {
        case Some(listener) => listener.apply(event)
        case None           => F.raiseError(new IllegalStateException(show"No listener for ${name.value} in $path"))
      }
    }

    def walk(html: HtmlReference[F], path: Path): F[HtmlHydratedReference[F]] = html.reference match {
      case reference: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
        reference.node.tag.listeners.keys
          .traverse { name =>
            val callback = dom.unsafeRun(listener(path, name, _))
            dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
          }
          .map { listeners =>
            val tag = reference.node.tag.copy(listeners = ListenerReferences.from(listeners))
            reference.copy(node = reference.node.copy(tag = tag))
          }
          .flatMap { reference =>
            reference.node.variant match {
              case Variant.Normal(children) =>
                children
                  .traverseWithKey((key, child) => walk(child, path / key))
                  .map { children =>
                    val variant = Node.Element.Variant.Normal(children)
                    reference.copy(node = reference.node.copy(variant = variant))
                  }
                  .flatMap { reference =>
                    reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
                      case Some((_, release)) => HtmlHydratedReference(reference, release)
                      case None               => HtmlHydratedReference(reference, F.unit)
                    }
                  }
              case Variant.Void =>
                reference.node.lifecycle.mount
                  .traverse(_.apply(reference.dom).allocated)
                  .map {
                    case Some((_, release)) => release
                    case None               => F.unit
                  }
                  .map { release =>
                    HtmlHydratedReference(
                      reference.asInstanceOf[NodeReference[F, ListenerReferences, HtmlHydratedReference[F]]],
                      release
                    )
                  }
            }
          }
      case reference: NodeReference.Fragment[HtmlReference[F]] =>
        reference.node.children
          .traverseWithKey((key, child) => walk(child, path / key))
          .map { children =>
            HtmlHydratedReference(reference.copy(node = reference.node.copy(children = children)), F.unit)
          }
      case reference: NodeReference.Text[F, Listeners[F]] =>
        reference.node.listeners.keys
          .traverse { name =>
            val callback = dom.unsafeRun(listener(path, name, _))
            dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
          }
          .map { listeners =>
            reference.copy(node = reference.node.copy(listeners = ListenerReferences.from(listeners)))
          }
          .flatMap { reference =>
            reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
              case Some((_, release)) => HtmlHydratedReference(reference, release)
              case None               => HtmlHydratedReference(reference, F.unit)
            }
          }

    }

    Kleisli(walk(_, Path.Root))
  }
}
