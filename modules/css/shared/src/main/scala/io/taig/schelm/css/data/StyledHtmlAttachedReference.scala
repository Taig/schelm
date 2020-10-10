package io.taig.schelm.css.data

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data.{HtmlAttachedReference, Path}
import io.taig.schelm.util.PathTraversal
import io.taig.schelm.util.PathTraversal.ops._

final case class StyledHtmlAttachedReference[F[_]](styles: Map[Selector, Style], html: HtmlAttachedReference[F])

object StyledHtmlAttachedReference {
  implicit def path[G[_]]: PathTraversal[StyledHtmlAttachedReference[G]] =
    new PathTraversal[StyledHtmlAttachedReference[G]] {
      override def get(value: StyledHtmlAttachedReference[G])(path: Path): Option[StyledHtmlAttachedReference[G]] =
        value.html.get(path).map(html => value.copy(html = html))

      override def modify[F[_]: Applicative](value: StyledHtmlAttachedReference[G])(path: Path)(
          f: StyledHtmlAttachedReference[G] => F[StyledHtmlAttachedReference[G]]
      ): F[StyledHtmlAttachedReference[G]] =
        value.html.modify(path)(haf => f(value.copy(html = haf)).map(_.html)).map(x => value.copy(html = x))
    }
}
