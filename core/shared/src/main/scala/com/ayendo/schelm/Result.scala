package com.ayendo.schelm

import cats.implicits._

final class Result[+A, +B](val state: Option[A], val commands: List[B])

object Result {
  val Empty: Result[Nothing, Nothing] = new Result(None, List.empty)

  def apply[A, B](state: A, commands: B*): Result[A, B] =
    new Result(state.some, commands.toList)

  def commands[A](commands: A*): Result[Nothing, A] =
    new Result(None, commands.toList)
}
