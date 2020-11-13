package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Hydrater}
import io.taig.schelm.data._
import io.taig.schelm.data.Node.Element.Variant
import alleycats.std.set._
import org.scalajs.dom.raw.Event

/** Receives an `HtmlReference`, triggers the lifecycle methods and initializes the listeners */
object HtmlHydrater {
  def apply[F[_]](dom: Dom[F], html: F[Html[F]])(
      implicit F: MonadCancel[F, Throwable]
  ): Hydrater[F, (HtmlReference[F], Path), HtmlHydratedReference[F]] = {
    def listeners(html: Html[F]): Option[Listeners[F]] = html.unfix match {
      case node: Node.Element[F, Html[F]] => Some(node.tag.listeners)
      case _: Node.Fragment[_]            => None
      case node: Node.Text[F]             => Some(node.listeners)
    }

    /** Create a listener that uses the `Path` and `Listener.Name` arguments to access the `Html` structure, find the
      * corresponding listener and triggers it
      */
    def listener(path: Path, name: Listener.Name, event: Event): F[Unit] = ???
//    html.flatMap { html =>
//      html.find(path).flatMap(listeners).flatMap(_.get(name)) match {
//        case Some(listener) => listener.apply(event)
//        case None           => F.raiseError(new IllegalStateException(show"No listener for ${name.value} in $path"))
//      }
//    }

    def element(reference: NodeReference.Element[F, HtmlReference[F]], path: Path): F[HtmlHydratedReference[F]] = ???
//      reference.node.tag.listeners.keys
//        .traverse { name =>
//          val callback = dom.unsafeRun(listener(path, name, _))
//          dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
//        }
//        .map { listeners =>
//          val tag = reference.node.tag.copy(listeners = ListenerReferences.from(listeners))
//          reference.copy(node = reference.node.copy(tag = tag))
//        }
//        .flatMap { reference =>
//          reference.node.variant match {
//            case Variant.Normal(children) =>
//              children
//                .traverseWithKey((key, child) => hydrate(child, path / key))
//                .map { children =>
//                  val variant = Node.Element.Variant.Normal(children)
//                  reference.copy(node = reference.node.copy(variant = variant))
//                }
//                .flatMap { reference =>
//                  reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
//                    case Some((_, release)) => HtmlHydratedReference(reference, release)
//                    case None               => HtmlHydratedReference(reference, F.unit)
//                  }
//                }
//            case Variant.Void =>
//              reference.node.lifecycle.mount
//                .traverse(_.apply(reference.dom).allocated)
//                .map {
//                  case Some((_, release)) => release
//                  case None               => F.unit
//                }
//                .map { release =>
//                  HtmlHydratedReference(
//                    reference.asInstanceOf[NodeReference[F, ListenerReferences, HtmlHydratedReference[F]]],
//                    release
//                  )
//                }
//          }
//        }

    def fragment(reference: NodeReference.Fragment[HtmlReference[F]], path: Path): F[HtmlHydratedReference[F]] = ???
//      reference.node.children
//        .traverseWithKey((key, child) => hydrate(child, path / key))
//        .map { children =>
//          val node = reference.node.copy(children = children)
//          HtmlHydratedReference(reference.copy(node = node), ListenerReferences.Empty, F.unit)
//        }

    def text(reference: NodeReference.Text[F], path: Path): F[HtmlHydratedReference[F]] = ???
//      reference.node.listeners.keys
//        .traverse { name =>
//          val callback = dom.unsafeRun(listener(path, name, _))
//          dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
//        }
//        .map { listeners => reference.copy(node = reference.node.copy(listeners = ListenerReferences.from(listeners))) }
//        .flatMap { reference =>
//          reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
//            case Some((_, release)) => HtmlHydratedReference(reference, release)
//            case None               => HtmlHydratedReference(reference, F.unit)
//          }
//        }

    def hydrate(html: HtmlReference[F], path: Path): F[HtmlHydratedReference[F]] = html.unfix match {
      case reference: NodeReference.Element[F, HtmlReference[F]] => element(reference, path)
      case reference: NodeReference.Fragment[HtmlReference[F]]   => fragment(reference, path)
      case reference: NodeReference.Text[F]                      => text(reference, path)
    }

    Kleisli((hydrate _).tupled)
  }
}
