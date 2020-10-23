package io.taig.schelm.documentation

final case class State(text: String)

object State {
  val Initial: State = State(text = "Lorem ipsum")
}
