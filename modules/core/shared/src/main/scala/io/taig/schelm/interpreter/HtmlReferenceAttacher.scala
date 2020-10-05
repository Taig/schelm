package io.taig.schelm.interpreter

import cats.effect.Bracket
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

final class HtmlReferenceAttacher[F[_]](attacher: Attacher[F, Vector[Dom.Node], Dom.Element])(
    implicit F: Bracket[F, Throwable]
) extends Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] {
  override def attach(html: HtmlReference[F]): F[HtmlAttachedReference[F]] =
    attacher.attach(html.dom) *> notify(html)

  def notify(html: HtmlReference[F]): F[HtmlAttachedReference[F]] = html.reference match {
    case NodeReference.Element(Node.Element(_, _, lifecycle), element) =>
      html.reference.traverse(notify).flatMap { reference =>
        lifecycle.mount.traverse(_.apply(element).allocated).map {
          case Some((_, release)) => HtmlAttachedReference(reference, release)
          case None               => HtmlAttachedReference(reference, F.unit)
        }
      }
    case NodeReference.Fragment(Node.Fragment(_)) =>
      html.reference.traverse(notify).map(HtmlAttachedReference(_, F.unit))
    case reference @ NodeReference.Text(Node.Text(_, _, lifecycle), text) =>
      lifecycle.mount.traverse(_.apply(text).allocated).map {
        case Some((_, release)) => HtmlAttachedReference(reference, release)
        case None               => HtmlAttachedReference(reference, F.unit)
      }
  }
}

object HtmlReferenceAttacher {
  def apply[F[_]: Bracket[*[_], Throwable]](
      attacher: Attacher[F, Vector[Dom.Node], Dom.Element]
  ): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] =
    new HtmlReferenceAttacher[F](attacher)

  def default[F[_]: Bracket[*[_], Throwable]](
      dom: Dom[F]
  )(root: Dom.Element): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] =
    HtmlReferenceAttacher(NodeAttacher(dom)(root))
}
