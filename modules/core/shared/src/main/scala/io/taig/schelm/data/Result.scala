package io.taig.schelm.data

final case class Result[+State, +Command](state: Option[State], commands: List[Command])

object Result {
  val Empty: Result[Nothing, Nothing] = Result(None, List.empty)
}
