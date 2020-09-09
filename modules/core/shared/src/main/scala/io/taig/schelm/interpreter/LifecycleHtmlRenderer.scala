//package io.taig.schelm.interpreter
//
//import cats.Monad
//import cats.implicits._
//import io.taig.schelm.algebra.{Dom, Renderer}
//import io.taig.schelm.data._
//
//object LifecycleHtmlRenderer {
//  def apply[F[+_]: Monad](dom: Dom[F]): Renderer[
//    F,
//    LifecycleHtml[F, dom.Node, dom.Element, dom.Text],
//    LifecycleHtmlReference[F, dom.Node, dom.Element, dom.Text]
//  ] =
//    new Renderer[
//      F,
//      LifecycleHtml[F, dom.Node, dom.Element, dom.Text],
//      LifecycleHtmlReference[F, dom.Node, dom.Element, dom.Text]
//    ] {
//      val renderer: Renderer[F, Component[LifecycleHtml[F, dom.Node, dom.Element, dom.Text]], NodeReference[
//            dom.Element,
//            dom.Text,
//            LifecycleHtmlReference[F, dom.Node, dom.Element, dom.Text]
//          ]] = ComponentRenderer(dom, this)(_.toHtmlReference.toNodes)
//
//      override def render(
//          html: LifecycleHtml[F, dom.Node, dom.Element, dom.Text]
//      ): F[LifecycleHtmlReference[F, dom.Node, dom.Element, dom.Text]] =
//        renderer.render(html.lifecycle.value).map { reference =>
//          LifecycleHtmlReference(html.lifecycle.copy(value = reference))
//        }
//    }
//}
