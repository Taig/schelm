package io.taig.schelm.css

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import fs2.Stream
import io.taig.schelm._
import io.taig.schelm.internal.EffectHelpers

object CssSchelm {
  val Id = "schelm-css"

  def enable[F[_]: Sync, State, Event, Node](
      globals: Stylesheet,
      render: State => Widget[Event]
  ): State => Html[Event] = { state =>
    val (html, stylesheet) = render(state).render
    ???
  }
//    CssRegistry[F].map { registry => state =>
//      val widget = render(state)
//
//      for {
//        _ <- registry.reset
//        html <- Widget.render(widget, registry)
//        stylesheet <- registry.snapshot.map(globals ++ _)
//        style <- getOrCreateStyleElement(dom)
//        _ <- dom.innerHtml(style, s"\n$stylesheet\n")
//      } yield html
//    }

  def getOrCreateStyleElement[F[_]: Sync, A, B](
      dom: Dom[F, A, B]
  ): F[dom.Element] =
    dom.getElementById(Id).flatMap {
      case Some(element) => element.pure[F]
      case None          => createStyleElement[F, A, B](dom)
    }

  def createStyleElement[F[_]: Sync, A, B](
      dom: Dom[F, A, B]
  ): F[dom.Element] =
    for {
      element <- dom.createElement("style")
      _ <- dom.setAttribute(element, "id", Id)
      head <- dom.head
      _ <- dom.appendChild(head, element)
    } yield element
}
