package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data._

object HtmlReferenceAttacher {
  def apply[F[_]: Applicative](attacher: Attacher[F, Vector[Dom.Node], Dom.Element]): Attacher[F, HtmlReference[F], Dom.Element] =
    new Attacher[F, HtmlReference[F], Dom.Element] {
      override def attach(html: HtmlReference[F]): F[Dom.Element] =
        attacher.attach(html.dom) <* notify(html)

      def notify(html: HtmlReference[F]): F[Unit] = html.reference match {
        case NodeReference.Element(Node.Element(_, _, lifecycle), element) =>
          html.reference.traverse_(notify) <* lifecycle.mounted.traverse_(_ apply element)
        case NodeReference.Fragment(Node.Fragment(_)) =>
          html.reference.traverse_(notify)
        case NodeReference.Stateful(_, reference) => notify(reference)
        case NodeReference.Text(Node.Text(_, _, lifecycle), text) =>
          lifecycle.mounted.traverse_(_ apply text)
      }
    }

  def default[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element): Attacher[F, HtmlReference[F], Dom.Element] =
    HtmlReferenceAttacher(NodeAttacher(dom)(parent))
}
