package io.taig.schelm.redux

import cats.data.Reader
import io.taig.schelm.redux.algebra.EventManager

package object data {
  type Redux[F[_], Event, A] = Reader[EventManager[F, Event], A]
}
