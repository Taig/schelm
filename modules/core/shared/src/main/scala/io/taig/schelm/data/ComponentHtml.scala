package io.taig.schelm.data

final case class ComponentHtml[F[_]](state: State[F, Node[F, Listeners[F], ComponentHtml[F]]])
