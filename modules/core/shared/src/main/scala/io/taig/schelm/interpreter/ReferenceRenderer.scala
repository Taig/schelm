package io.taig.schelm.interpreter

import cats.Functor
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.Reference

final class ReferenceRenderer[F[_]: Functor, Event, View, Structure](renderer: Renderer[F, View, Structure])
    extends Renderer[F, View, Reference[Structure, View]] {
  override def render(view: View): F[Reference[Structure, View]] = ???
}
