package io.taig.schelm.css

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm._
import org.scalajs.dom

object Css {
  val Id = "schelm-css"

  def enable[F[_]: Sync, State, Event](
      globals: Stylesheet,
      render: State => Widget[Event]
  ): F[State => F[Html[Event]]] =
    CssRegistry[F].map { registry => state =>
      val widget = render(state)

      for {
        _ <- registry.reset
        html <- Widget.render(widget, registry)
        stylesheet <- registry.snapshot.map(globals ++ _)
        style <- getOrCreateStyleElement[F]
//        _ <- Dom.innerHtml(style, s"\n$stylesheet\n")
      } yield html
    }

  def getOrCreateStyleElement[F[_]: Sync]: F[dom.Element] = ???
//    Dom.getElementById(Id).flatMap {
//      case Some(element) => element.pure[F]
//      case None          => createStyleElement[F]
//    }

  def createStyleElement[F[_]: Sync]: F[dom.Element] = ???
//    for {
//      element <- Dom.createElement[F]("style")
//      _ <- Dom.setAttribute[F](element, "id", Id)
//      head <- Dom.head[F]
//      _ <- Dom.append[F](head, element)
//    } yield element
}
