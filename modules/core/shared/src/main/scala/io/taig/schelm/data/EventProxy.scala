package io.taig.schelm.data

import cats.effect.kernel.Sync
import org.scalajs.dom.raw.{Event, EventTarget, HTMLInputElement}

abstract class EventProxy[F[_], A] {
  def native: Event

  def preventDefault: F[Unit]

  def currentTarget: A

  def target: EventTargetProxy[F]
}

object EventTargetProxy {
  def fromEvent[F[_]](event: Event)(implicit F: Sync[F]): EventProxy[F, EventTargetProxy[F]] =
    new EventProxy[F, EventTargetProxy[F]] {
      override def native: Event = event

      override def preventDefault: F[Unit] = F.delay(event.preventDefault())

      override def currentTarget: EventTargetProxy[F] = ???

      override def target: EventTargetProxy[F] = ???
    }
}

abstract class EventTargetProxy[F[_]] {
  def native: EventTarget
}

object EventTarget {
  def fromEventTarget[F[_]](target: EventTarget): EventTargetProxy[F] = new EventTargetProxy[F] {
    override def native: EventTarget = target
  }
}

abstract class InputEventTargetProxy[F[_]] extends EventTargetProxy[F] {
  def value: F[String]
}

object InputEventTargetProxy {
  def fromInputElement[F[_]](target: HTMLInputElement)(implicit F: Sync[F]): InputEventTargetProxy[F] =
    new InputEventTargetProxy[F] {
      override def native: EventTarget = target

      override def value: F[String] = F.delay(target.value)
    }
}
