package io.taig.schelm.dsl.syntax

import cats.effect.kernel.Sync
import cats.implicits._
import io.taig.schelm.data
import io.taig.schelm.data.{Listener, Listeners}
import io.taig.schelm.dsl.data.DslListener
import org.scalajs.dom.raw.Event

trait listener {
  implicit class ListenerNameOps(name: data.Listener.Name) {
    def :=[F[_]](action: Listener.Action[F]): DslListener[F] = Listener(name, action)
  }

  @inline
  def listeners[F[_], A](values: Listener[F]*): Listeners[F] = Listeners.from(values)

  object action {
    @inline
    def apply[F[_]](f: Event => F[Unit]): Listener.Action[F] = f

    def run[F[_]](fa: F[Unit]): Listener.Action[F] = action(_ => fa)

    def target[F[_], A](f: A => F[Unit])(implicit F: Sync[F]): Listener.Action[F] = action { event =>
      F.delay(event.currentTarget.asInstanceOf[A]).flatMap(f)
    }
  }

  val onChange: Listener.Name = Listener.Name("change")
  val onClick: Listener.Name = Listener.Name("click")
  val onInput: Listener.Name = Listener.Name("input")
}

object listener extends listener
