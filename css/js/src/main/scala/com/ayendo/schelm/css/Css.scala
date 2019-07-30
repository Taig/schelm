package com.ayendo.schelm.css

import cats.effect.{Concurrent, Sync}
import cats.implicits._
import com.ayendo.schelm._
import org.scalajs.dom

object Css {
  val Id = "schelm-css"

  def enable[F[_]: Sync, State, Event](
      registry: StylesRegistry[F],
      render: State => Widget[Event]
  ): State => F[Html[Event]] = { state =>
    val widget = render(state)

    for {
      _ <- registry.reset
      html <- Widget.render(widget, registry)
      stylesheet <- registry.snapshot
      style <- getOrCreateStyleElement[F]
      _ <- Dom.innerHtml(style, s"\n$stylesheet\n")
    } yield html
  }

  def enable[F[_]: Concurrent, State, Event](
      render: State => Widget[Event]
  ): F[State => F[Html[Event]]] =
    StylesRegistry[F](Stylesheet.Empty).map(enable[F, State, Event](_, render))

  def getOrCreateStyleElement[F[_]: Sync]: F[dom.Element] =
    Dom.getElementById(Id).flatMap {
      case Some(element) => element.pure[F]
      case None          => createStyleElement[F]
    }

  def createStyleElement[F[_]: Sync]: F[dom.Element] =
    for {
      element <- Dom.createElement[F]("style")
      _ <- Dom.setAttribute[F](element, "id", Id)
      head <- Dom.head[F]
      _ <- Dom.append[F](head, element)
    } yield element
}
