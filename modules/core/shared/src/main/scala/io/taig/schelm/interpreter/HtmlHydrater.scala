package io.taig.schelm.interpreter

import scala.annotation.tailrec

import cats.Eval
import cats.data.Kleisli
import cats.effect.MonadCancel
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Hydrater}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._
import io.taig.schelm.implicits._
import org.scalajs.dom.raw.Event

/** Receives an `HtmlReference`, triggers the lifecycle methods and initializes the listeners */
object HtmlHydrater {
  def apply[F[_]](dom: Dom[F], identifiers: F[IdentifierTree[Eval[Html[F]]]])(
      implicit F: MonadCancel[F, Throwable]
  ): Hydrater[F, (NamespaceHtmlReference[F], Path), HtmlHydratedReference[F]] = {
    val Noop = F.unit

    /** Create a listener that uses the `Path` and `Listener.Name` arguments to access the `Html` structure, find the
      * corresponding listener and triggers it
      */
    def listener(path: Path, name: Listener.Name, event: Event): F[Unit] = identifiers.flatMap { identifiers =>
      identifiers
        .find(path.identification)
        .map(_.value.value)
        .flatMap(_.select(path.indices))
        .flatMap(_.listeners)
        .flatMap(_.get(name))
        .liftTo[F](new IllegalStateException(show"No listener for $name in $path"))
        .flatMap(_.apply(event))
    }

    def element(
        reference: NodeReference.Element[F, NamespaceHtmlReference[F]],
        path: Path
    ): F[HtmlHydratedReference[F]] =
      reference.node.tag.listeners.keys.toList
        .traverse { name =>
          val callback = dom.unsafeRun(listener(path, name, _))
          dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
        }
        .map(ListenerReferences.from)
        .flatMap { listeners =>
          reference.node.lifecycle.mount
            .traverse(_.apply(reference.dom).allocated)
            .map {
              case Some((_, release)) => release
              case None               => Noop
            }
            .flatMap { release =>
              reference.node.variant match {
                case Variant.Normal(children) =>
                  children.traverse(hydrate(_, path)).map { children =>
                    HtmlHydratedReference(
                      reference.copy(node = reference.node.copy(variant = Node.Element.Variant.Normal(children))),
                      listeners,
                      Noop
                    )
                  }
                case Variant.Void =>
                  HtmlHydratedReference(
                    reference.asInstanceOf[NodeReference[F, HtmlHydratedReference[F]]],
                    listeners,
                    release
                  ).pure[F]
              }
            }
        }

    def fragment(
        reference: NodeReference.Fragment[NamespaceHtmlReference[F]],
        path: Path
    ): F[HtmlHydratedReference[F]] = reference.node.children.traverse(hydrate(_, path)).map { children =>
      HtmlHydratedReference(
        reference.copy(node = reference.node.copy(children = children)),
        ListenerReferences.Empty,
        Noop
      )
    }

    def text(reference: NodeReference.Text[F], path: Path): F[HtmlHydratedReference[F]] =
      reference.node.listeners.keys.toList
        .traverse { name =>
          val callback = dom.unsafeRun(listener(path, name, _))
          dom.addEventListener(reference.dom, name.value, callback).as(name -> callback)
        }
        .map(ListenerReferences.from)
        .flatMap { listeners =>
          reference.node.lifecycle.mount
            .traverse(_.apply(reference.dom).allocated)
            .map {
              case Some((_, release)) => release
              case None               => Noop
            }
            .map(HtmlHydratedReference(reference, listeners, _))
        }

    @tailrec
    def hydrate(namespace: NamespaceHtmlReference[F], path: Path): F[HtmlHydratedReference[F]] = namespace.unfix match {
      case namespace: Namespace.Identified[NodeReference[F, Fix[λ[A => Namespace[NodeReference[F, A]]]]]] =>
        hydrate(NamespaceHtmlReference(namespace.namespace), path / namespace.identifier)
      case namespace: Namespace.Anonymous[NodeReference[F, Fix[λ[A => Namespace[NodeReference[F, A]]]]]] =>
        namespace.value match {
          case reference: NodeReference.Element[F, NamespaceHtmlReference[F]] => element(reference, path)
          case reference: NodeReference.Fragment[NamespaceHtmlReference[F]]   => fragment(reference, path)
          case reference: NodeReference.Text[F]                               => text(reference, path)
        }
    }

    Kleisli((hydrate _).tupled)
  }

  def root[F[_]](dom: Dom[F], identifiers: F[IdentifierTree[Eval[Html[F]]]])(
      implicit F: MonadCancel[F, Throwable]
  ): Hydrater[F, NamespaceHtmlReference[F], HtmlHydratedReference[F]] =
    HtmlHydrater[F](dom, identifiers).local((_, Path.Root))
}
