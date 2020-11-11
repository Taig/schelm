package io.taig.schelm.data

final case class StateHtml[F[_]](value: State[F, Node[F, StateHtml[F]]]) extends AnyVal
