package io.taig.schelm.documentation

import cats.effect.Sync
import io.taig.schelm.data.{Children, Listeners}
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property
import io.taig.schelm.material.{MaterialButton, MaterialElevation, MaterialInput, MaterialTheme, MaterialTypography}

object App {
  def apply[F[_]](state: State)(implicit F: Sync[F]): Widget[F, Event, MaterialTheme] = {
    syntax.html.div(
      children = Children.of(
        MaterialTypography.h3("Buttons"),
        MaterialTypography.body1("This is the textual content above le buttons!"),
        MaterialButton
          .themed("hello world", tag = MaterialButton.Tag.A, variant = Some(MaterialButton.Variant.Primary)),
        MaterialElevation(
          MaterialButton.themed(
            "hello world",
            tag = MaterialButton.Tag.Button,
            property = Property(listeners = Listeners.of(click := { _ => F.delay(println("hi")) }))
          ),
          clickable = true
        ),
        MaterialButton.themed("hello world", tag = MaterialButton.Tag.Input(tpe = "submit")),
        MaterialTypography.h3("Input"),
        MaterialInput.themed(label = Some("Address"), placeholder = Some("Hasenheide 8"), id = Some("h8")),
        MaterialInput.themed(
          label = Some("Name"),
          placeholder = Some("Demiank"),
          id = Some("name"),
          variant = MaterialInput.Variant.Error,
          helper = Some("Dit war nix"),
          properties = MaterialInput.Properties(input = Property(listeners = Listeners.of(input := { event =>
            F.delay(println(event))
          })))
        ),
        MaterialInput.themed(
          label = Some("Name"),
          placeholder = Some("Demiank"),
          id = Some("foobar"),
          variant = MaterialInput.Variant.Success,
          helper = Some("Dit war nix")
        ),
        MaterialInput.themed(
          label = Some("Name"),
          placeholder = Some("Demiank"),
          id = Some("yolo"),
          variant = MaterialInput.Variant.Warning,
          helper = None
        )
      )
    )
  }
}
