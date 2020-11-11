package io.taig.schelm.interpreter

import cats.Applicative
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

/** Attach a `Vector` of `Node`s to a parent `Element` and return the parent  */
object NodeAttacher {
  def apply[F[_]: Applicative](dom: Dom[F])(root: Dom.Element): Attacher[F, Vector[Dom.Node], Dom.Element] =
    Kleisli(_.traverse_(dom.appendChild(root, _)).as(root))
}
