package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer, StateManager}
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Sync](dom: Dom[F], manager: StateManager[F])
    extends Renderer[F, Html[F], HtmlReference[F]] { self =>
  override def render(html: Html[F]): F[HtmlReference[F]] = html.node match {
    case node @ Node.Element(tag, Node.Element.Variant.Normal(children), _) =>
      for {
        element <- dom.createElement(tag.name)
        _ <- tag.attributes.toList.traverse_ { attribute =>
          dom.setAttribute(element, attribute.key.value, attribute.value.value)
        }
        listeners <- tag.listeners.toList
          .traverse { listener =>
            val reference = dom.unsafeRun(listener.action)
            dom
              .addEventListener(element, listener.name.value, reference)
              .as(listener.name -> ((reference, listener.action)))
          }
          .map(listeners => ListenerReferences(listeners.toMap))
        children <- children.traverse(render)
        _ <- children.toList.flatMap(_.dom).traverse_(dom.appendChild(element, _))
        variant = Node.Element.Variant.Normal(children)
        reference = NodeReference.Element(node.copy(tag = tag.copy(listeners = listeners), variant = variant), element)
      } yield HtmlReference(reference)
    case node @ Node.Element(tag, Node.Element.Variant.Void, _) =>
      for {
        element <- dom.createElement(tag.name)
        _ <- tag.attributes.toList.traverse_ { attribute =>
          dom.setAttribute(element, attribute.key.value, attribute.value.value)
        }
        listeners <- tag.listeners.toList
          .traverse { listener =>
            val reference = dom.unsafeRun(listener.action)
            dom
              .addEventListener(element, listener.name.value, reference)
              .as(listener.name -> ((reference, listener.action)))
          }
          .map(listeners => ListenerReferences(listeners.toMap))
        reference = NodeReference
          .Element(node.copy(tag = tag.copy(listeners = listeners), variant = Node.Element.Variant.Void), element)
      } yield HtmlReference(reference)
    case node @ Node.Fragment(children) =>
      children
        .traverse(render)
        .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))
    case node @ Node.Stateful(initial, render) =>
      for {
        result <- Ref[F].of(none[HtmlReference[F]])
        update = new (Any => F[Unit]) {
          override def apply(value: Any): F[Unit] =
            result.get
              .flatMap(_.liftTo[F](new IllegalStateException))
              .map(StateManager.Event(node, _, value, this))
              .flatMap(manager.submit)
        }
        html = render(update, initial)
        reference <- self.render(html)
        _ <- result.set(reference.some)
      } yield reference
    case node @ Node.Text(value, listeners, _) =>
      for {
        text <- dom.createTextNode(value)
        listeners <- listeners.toList
          .traverse { listener =>
            val reference = dom.unsafeRun(listener.action)
            dom
              .addEventListener(text, listener.name.value, reference)
              .as(listener.name -> ((reference, listener.action)))
          }
          .map(listeners => ListenerReferences(listeners.toMap))
      } yield HtmlReference(NodeReference.Text(node.copy(listeners = listeners), text))
  }
}

object HtmlRenderer {
  def apply[F[_]: Sync](dom: Dom[F], manager: StateManager[F]): Renderer[F, Html[F], HtmlReference[F]] =
    new HtmlRenderer[F](dom, manager)
}
