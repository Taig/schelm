package io.taig.schelm.css.data

import io.taig.schelm.data.HtmlHydratedReference

final case class StyledHtmlHydratedReference[F[_]](styles: Map[Selector, Style], html: HtmlHydratedReference[F])

object StyledHtmlHydratedReference {
//  implicit def path[G[_]]: PathTraversal[StyledHtmlAttachedReference[G]] =
//    new PathTraversal[StyledHtmlAttachedReference[G]] {
//      override def find(value: StyledHtmlAttachedReference[G])(path: Path): Option[StyledHtmlAttachedReference[G]] =
//        value.html.get(path).map(html => value.copy(html = html))
//
//      override def modify[F[_]: Applicative](value: StyledHtmlAttachedReference[G])(path: Path)(
//          f: StyledHtmlAttachedReference[G] => F[StyledHtmlAttachedReference[G]]
//      ): F[StyledHtmlAttachedReference[G]] =
//        value.html.modify(path)(haf => f(value.copy(html = haf)).map(_.html)).map(x => value.copy(html = x))
//    }
}
