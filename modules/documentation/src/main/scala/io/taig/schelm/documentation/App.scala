package io.taig.schelm.documentation

import cats.effect.Sync
import io.taig.schelm.data.{Children, Listeners}
import io.taig.schelm.dsl._
import io.taig.schelm.material.{MaterialButton, MaterialElevation, MaterialInput, MaterialTheme, MaterialTypography}

object App {
  def apply[F[_]](state: State)(implicit F: Sync[F]): Widget[F, Event, MaterialTheme] = {
    div(
      children = Children.of(
        MaterialTypography.h3("Buttons"),
        MaterialTypography.body1("This is the textual content above le buttons!"),
        MaterialButton
          .default("hello world", tag = MaterialButton.Tag.A, flavor = Some(MaterialButton.Flavor.Primary)),
        MaterialButton.default("hello world", tag = MaterialButton.Tag.Button, listeners = Listeners.of(click := { _ =>
          F.delay(println("hi"))
        })),
        MaterialElevation(
          MaterialButton.default("hello world", tag = MaterialButton.Tag.Input(tpe = "submit")),
          clickable = true
        ),
        MaterialTypography.h3("Input"),
        MaterialInput.default(label = "Address", placeholder = Some("Hasenheide 8"))
      )
    )
  }
}
