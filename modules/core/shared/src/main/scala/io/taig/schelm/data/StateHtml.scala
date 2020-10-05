package io.taig.schelm.data

final case class StateHtml[F[_]](state: State[F, Node[F, Listeners[F], StateHtml[F]]])
