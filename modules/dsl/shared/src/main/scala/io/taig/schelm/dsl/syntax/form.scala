package io.taig.schelm.dsl.syntax

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.data.Listener
import io.taig.schelm.dsl.Widget
import io.taig.schelm.dsl.data.Property
import io.taig.schelm.dsl.syntax.html._
import io.taig.schelm.dsl.syntax
import io.taig.schelm.dsl.syntax.attribute._
import io.taig.schelm.dsl.syntax.listener._
import org.scalajs.dom.raw.{Event, HTMLInputElement}

trait form {
  object ManagedInput {
    def apply[F[_]](value: String, property: Property[F] = Property.Empty)(
        implicit F: Sync[F]
    ): Widget[F, Nothing, Any] = {
//      val underlying = property.listeners.get(listener.onInput).getOrElse(listener.effect.noop)
//      val interception: Listener[F, Event, HTMLInputElement] = onInput := effect { (event, target) =>
//        for {
//          _ <- F.delay(target.value = value)
//          _ <- Listener.Action.run(underlying, event, target)
//        } yield ()
//      }
//
//      val update = property.appendAttributes(attrs(syntax.attribute.value := value)).addListener(interception)
//      input()
      ???
    }
  }
}

object form extends form
