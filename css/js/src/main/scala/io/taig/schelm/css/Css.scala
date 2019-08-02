package io.taig.schelm.css

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm._

object Css {
  val Id = "schelm-css"

  def enable[F[_]: Sync, State, Event, B](
      globals: Stylesheet,
      dom: Dom[F, Event, B],
      render: State => Widget[Event]
  ): F[State => F[Html[Event]]] =
    CssRegistry[F].map { registry => state =>
      val widget = render(state)

      for {
        _ <- registry.reset
        html <- Widget.render(widget, registry)
        stylesheet <- registry.snapshot.map(globals ++ _)
        style <- getOrCreateStyleElement(dom)
        _ <- dom.innerHtml(style, s"\n$stylesheet\n")
      } yield html
    }

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
