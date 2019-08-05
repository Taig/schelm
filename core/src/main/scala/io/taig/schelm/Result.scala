package io.taig.schelm

import cats.implicits._

final class Result[+State, +Command](
    val state: Option[State],
    val commands: List[Command]
)

object Result {
  val Empty: Result[Nothing, Nothing] = new Result(None, List.empty)

  def apply[A, B](state: A, commands: B*): Result[A, B] =
    new Result(state.some, commands.toList)

  def commands[A](commands: A*): Result[Nothing, A] =
    new Result(None, commands.toList)
}
