package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.LiftIO
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

object HtmlReferenceAttacher {
  def apply[F[_]: Monad: LiftIO, Event](platform: Platform)(
      attacher: Attacher[F, List[platform.Node], platform.Element]
  ): Attacher[
    F,
    HtmlReference[Event, platform.Node, platform.Element, platform.Text],
    HtmlAttachedReference[Event, platform.Node, platform.Element, platform.Text]
  ] =
    new Attacher[
      F,
      HtmlReference[Event, platform.Node, platform.Element, platform.Text],
      HtmlAttachedReference[Event, platform.Node, platform.Element, platform.Text]
    ] {
      override def attach(
          html: HtmlReference[Event, platform.Node, platform.Element, platform.Text]
      ): F[HtmlAttachedReference[Event, platform.Node, platform.Element, platform.Text]] =
        attacher.attach(html.toNodes) *> notify(html)

      def notify(
          html: HtmlReference[Event, platform.Node, platform.Element, platform.Text]
      ): F[HtmlAttachedReference[Event, platform.Node, platform.Element, platform.Text]] = {
        html.reference match {
          case NodeReference.Element(Node.Element(_, _, lifecycle), element) =>
            html.reference.traverse(notify(_)).flatMap { reference =>
              lifecycle.apply(platform)(element).allocated.to[F].map {
                case (_, release) => HtmlAttachedReference(reference, release)
              }
            }
          case NodeReference.Fragment(Node.Fragment(_, lifecycle)) =>
            html.reference.traverse(notify(_)).flatMap { reference =>
              lifecycle.apply(platform)(html.toNodes).allocated.to[F].map {
                case (_, release) => HtmlAttachedReference(reference, release)
              }
            }
          case reference @ NodeReference.Text(Node.Text(_, _, lifecycle), text) =>
            lifecycle.apply(platform)(text).allocated.to[F].map {
              case (_, release) => HtmlAttachedReference(reference, release)
            }
        }
      }
    }

  def default[F[_]: Monad: LiftIO, Event](dom: Dom[F])(parent: dom.Element): Attacher[
    F,
    HtmlReference[Event, dom.Node, dom.Element, dom.Text],
    HtmlAttachedReference[Event, dom.Node, dom.Element, dom.Text]
  ] = HtmlReferenceAttacher(dom)(NodeAttacher(dom)(parent))
}
