//package io.taig.schelm.css.interpreter
//
//import cats.Applicative
//import cats.effect.Bracket
//import cats.implicits._
//import io.taig.schelm.algebra.{Attacher, Dom}
//import io.taig.schelm.css.data.{Selector, Style}
//import io.taig.schelm.data.{HtmlAttachedReference, HtmlReference}
//import io.taig.schelm.interpreter.HtmlReferenceAttacher
//
//object CssHtmlAttacher {
//  def apply[F[_]: Applicative, A, B](
//      html: Attacher[F, HtmlReference[F], A],
//      css: Attacher[F, Map[Selector, Style], B]
//  ): Attacher[F, (HtmlReference[F], Map[Selector, Style]), (A, B)] = {
//      override def attach(structure: (HtmlReference[F], Map[Selector, Style])): F[(A, B)] = {
//        val (nodes, styles) = structure
//        (html.attach(nodes), css.attach(styles)).tupled
//      }
//    }
//
//  def default[F[_]: Bracket[*[_], Throwable]](dom: Dom[F])(main: Dom.Element): F[
//    Attacher[F, (HtmlReference[F], Map[Selector, Style]), (HtmlAttachedReference[F], Dom.Element)]
//  ] =
//    CssStyleAttacher.auto(dom, CssRenderer[F]).map(CssHtmlAttacher(HtmlReferenceAttacher.default(dom)(main), _))
//}
