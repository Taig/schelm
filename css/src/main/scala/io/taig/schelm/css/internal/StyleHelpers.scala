package io.taig.schelm.css.internal

import cats.Monad
import cats.implicits._
import io.taig.schelm.Dom

object StyleHelpers {
  val Id = "schelm-css"

  def getOrCreateStyleElement[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): F[dom.Element] =
    dom.getElementById(Id).flatMap {
      case Some(element) => element.pure[F]
      case None          => createStyleElement(dom)
    }

  def createStyleElement[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): F[dom.Element] =
    for {
      element <- dom.createElement("style")
      _ <- dom.setAttribute(element, "id", Id)
      head <- dom.head
      _ <- dom.appendChild(head, element)
    } yield element
}
