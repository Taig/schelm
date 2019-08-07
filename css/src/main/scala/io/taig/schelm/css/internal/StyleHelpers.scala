package io.taig.schelm.css.internal

import cats.Monad
import cats.implicits._
import io.taig.schelm._

object StyleHelpers {
  val Id = "schelm-css"

  def getOrCreateStyleElement[F[_]: Monad, Event](
      dom: Dom[F, Event]
  ): F[Element] =
    dom.getElementById(Id).flatMap {
      case Some(element) => element.pure[F]
      case None          => createStyleElement(dom)
    }

  def createStyleElement[F[_]: Monad, Event](
      dom: Dom[F, Event]
  ): F[Element] =
    for {
      element <- dom.createElement("style")
      _ <- dom.setAttribute(element, "id", Id)
      head <- dom.head
      _ <- dom.appendChild(head, element)
    } yield element
}
