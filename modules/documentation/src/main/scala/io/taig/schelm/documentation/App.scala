package io.taig.schelm.documentation

import cats.effect.Sync
import io.taig.schelm.data.Children
import io.taig.schelm.dsl._
import io.taig.schelm.dsl.data.Property
import io.taig.schelm.material._
import io.taig.schelm.redux.algebra.EventManager

object App {
  def apply[F[_]](state: State)(implicit F: Sync[F]): Widget[F, Event, MaterialTheme] =
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
            property = Property(
              listeners = listeners(
                click := effect.run(F.delay(println("hi")))
              )
            )
          ),
          clickable = true
        ),
        MaterialButton.themed("hello world", tag = MaterialButton.Tag.Input(tpe = "submit")),
        MaterialTypography.h3("Input"),
        MaterialInput.themed(label = Some("Address"), placeholder = Some("Hasenheide 8"), id = Some("h8")),
        eventful { (events: EventManager[F, Event]) =>
          MaterialInput.themed(
            label = Some("Name"),
            placeholder = Some("Demiank"),
            id = Some("name"),
            value = Some(state.text),
            variant = MaterialInput.Variant.Error,
            helper = Some("Dit war nix"),
            onInput = effect.target(input => events.submit(Event.TextChanged(input.value)))
          )
        },
        MaterialTypography.body1(s"Your message to Demian, published via global event handlers: ${state.text}"),
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
