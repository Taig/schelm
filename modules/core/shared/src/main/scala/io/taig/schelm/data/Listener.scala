package io.taig.schelm.data

import cats.Show
import org.scalajs.dom.raw.Event

final case class Listener[+F[_]](name: Listener.Name, action: Listener.Action[F]) {
  def toTuple: (Listener.Name, Listener.Action[F]) = (name, action)
}

object Listener {
  final case class Name(value: String) extends AnyVal

  object Name {
    implicit val show: Show[Name] = _.value
  }

  type Action[+F[_]] = Event => F[Unit]
}
