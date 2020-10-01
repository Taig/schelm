package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

final class HtmlReferenceAttacher[F[_]](attacher: Attacher[F, Vector[Dom.Node], Dom.Element])(implicit F: Concurrent[F])
    extends Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] {
  override def attach(html: HtmlReference[F]): F[HtmlAttachedReference[F]] =
    attacher.attach(html.dom) *> notify(html)

  def notify(html: HtmlReference[F]): F[HtmlAttachedReference[F]] = html.reference match {
    case NodeReference.Element(Node.Element(_, _, lifecycle), element) =>
      html.reference.traverse(notify).flatMap { reference =>
        lifecycle.apply(element).allocated.map {
          case (_, release) => HtmlAttachedReference(reference, release)
        }
      }
    case NodeReference.Fragment(Node.Fragment(_)) =>
      html.reference.traverse(notify).map(HtmlAttachedReference(_, F.unit))
    case reference @ NodeReference.Stateful(_, value) =>
      notify(value).map(value => HtmlAttachedReference(reference.copy(value = value), F.unit))
    case reference @ NodeReference.Text(Node.Text(_, _, lifecycle), text) =>
      lifecycle.apply(text).allocated.map {
        case (_, release) => HtmlAttachedReference(reference, release)
      }
  }
}

object HtmlReferenceAttacher {
  def apply[F[_]: Concurrent](
      attacher: Attacher[F, Vector[Dom.Node], Dom.Element]
  ): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] =
    new HtmlReferenceAttacher[F](attacher)

  def default[F[_]: Concurrent](
      dom: Dom[F]
  )(parent: Dom.Element): Attacher[F, HtmlReference[F], HtmlAttachedReference[F]] =
    HtmlReferenceAttacher(NodeAttacher(dom)(parent))
}
