package io.taig.schelm.interpreter

import cats.data.Kleisli
import io.taig.schelm.algebra.Hydrater
import io.taig.schelm.data.{HtmlAttachedReference, HtmlHydratedReference, HtmlReference, Listeners, Node, NodeReference}

object HtmlHydrater {
  def apply[F[_]]: Hydrater[F, HtmlReference[F], HtmlHydratedReference[F]] = {
    Kleisli { html =>
      html.reference match {
        case node: NodeReference.Element[F, Listeners[F], HtmlReference[F]] =>
          node
          ???
        case node: NodeReference.Fragment[HtmlReference[F]] =>
          ???
        case node: NodeReference.Text[F, Listeners[F]] =>
          ???
      }
    }
  }
}
