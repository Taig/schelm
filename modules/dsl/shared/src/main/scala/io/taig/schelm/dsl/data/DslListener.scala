package io.taig.schelm.dsl.data

import io.taig.schelm.data.Listener

final case class DslListener[F[_]](name: Listener.Name, action: Option[Listener.Action[F]])
