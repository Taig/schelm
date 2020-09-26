package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.LiftIO
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

object HtmlReferenceAttacher {
  def apply[F[_]: Monad: LiftIO, Event](
      attacher: Attacher[F, List[Dom.Node], Dom.Element]
  ): Attacher[F, HtmlReference[Event], HtmlAttachedReference[Event]] =
    new Attacher[F, HtmlReference[Event], HtmlAttachedReference[Event]] {
      override def attach(html: HtmlReference[Event]): F[HtmlAttachedReference[Event]] =
        attacher.attach(html.toNodes) *> notify(html)

      def notify(html: HtmlReference[Event]): F[HtmlAttachedReference[Event]] = {
        html.reference match {
          case NodeReference.Element(Node.Element(_, _, lifecycle), element) =>
            html.reference.traverse(notify).flatMap { reference =>
              lifecycle.apply(element).allocated.to[F].map {
                case (_, release) => HtmlAttachedReference(reference, release)
              }
            }
          case NodeReference.Fragment(Node.Fragment(_, lifecycle)) =>
            html.reference.traverse(notify).flatMap { reference =>
              lifecycle.apply(html.toNodes).allocated.to[F].map {
                case (_, release) => HtmlAttachedReference(reference, release)
              }
            }
          case reference @ NodeReference.Text(Node.Text(_, _, lifecycle), text) =>
            lifecycle.apply(text).allocated.to[F].map {
              case (_, release) => HtmlAttachedReference(reference, release)
            }
        }
      }
    }

  def default[F[_]: Monad: LiftIO, Event](
      dom: Dom[F]
  )(parent: Dom.Element): Attacher[F, HtmlReference[Event], HtmlAttachedReference[Event]] =
    HtmlReferenceAttacher(NodeAttacher(dom)(parent))
}
