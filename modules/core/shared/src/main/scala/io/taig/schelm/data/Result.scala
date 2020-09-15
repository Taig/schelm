package io.taig.schelm.data

import cats.{Bifunctor, Functor}

final case class Result[+State, +Command](state: Option[State], commands: List[Command])

object Result {
  val Empty: Result[Nothing, Nothing] = Result(None, List.empty)

  implicit val bifunctor: Bifunctor[Result] = new Bifunctor[Result] {
    override def bimap[A, B, C, D](fab: Result[A, B])(f: A => C, g: B => D): Result[C, D] =
      Result(fab.state.map(f), fab.commands.map(g))
  }

  implicit def functor[State]: Functor[Result[State, *]] = bifunctor.rightFunctor
}
