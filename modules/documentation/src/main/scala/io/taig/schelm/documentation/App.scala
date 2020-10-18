package io.taig.schelm.documentation

import cats.effect.Sync
import io.taig.schelm.data.{Children, Listeners}
import io.taig.schelm.dsl._
import io.taig.schelm.material.{MaterialButton, MaterialTheme}

object App {
  def apply[F[_]](state: State)(implicit F: Sync[F]): Widget[F, Event, MaterialTheme] = {
    div(
      children = Children.of(
        MaterialButton.default("hello world", tag = MaterialButton.Tag.A, flavor = Some(MaterialButton.Flavor.Primary)),
        MaterialButton.default("hello world", tag = MaterialButton.Tag.Button, listeners = Listeners.of(click := {
          (_) => F.delay(println("hi"))
        })),
        MaterialButton.default("hello world", tag = MaterialButton.Tag.Input(tpe = "submit"))
      )
    )
  }
}
