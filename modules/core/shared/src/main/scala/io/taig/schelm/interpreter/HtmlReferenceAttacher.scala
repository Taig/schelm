package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.effect.Bracket
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

object HtmlReferenceAttacher {
  def apply[F[_]](
      attacher: Attacher[F, Vector[Dom.Node], Dom.Element]
  )(implicit F: Bracket[F, Throwable]): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] = {
    def notify(html: HtmlReference[F]): F[HtmlAttachedReference[F]] = html.reference match {
      case reference: NodeReference.Element[F, HtmlReference[F]] =>
        val element = reference.dom
        val lifecycle = reference.node.lifecycle

        html.reference.traverse(notify).flatMap { reference =>
          lifecycle.mount.traverse(_.apply(element).allocated).map {
            case Some((_, release)) => HtmlAttachedReference(reference, release)
            case None               => HtmlAttachedReference(reference, F.unit)
          }
        }
      case NodeReference.Fragment(Node.Fragment(_)) =>
        html.reference.traverse(notify).map(HtmlAttachedReference(_, F.unit))
      case reference: NodeReference.Text[F] =>
        reference.node.lifecycle.mount.traverse(_.apply(reference.dom).allocated).map {
          case Some((_, release)) => HtmlAttachedReference(reference, release)
          case None               => HtmlAttachedReference(reference, F.unit)
        }
    }

    Kleisli(html => attacher.run(html.dom) *> notify(html))
  }

  def default[F[_]: Bracket[*[_], Throwable]](
      dom: Dom[F]
  )(root: Dom.Element): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] =
    HtmlReferenceAttacher(NodeAttacher(dom)(root))
}
