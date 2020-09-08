package io.taig.schelm.algebra

import cats.effect.IO
import io.taig.schelm.data.Listener
import io.taig.schelm.data.Listener.Action

abstract class Dispatcher[F[_], Event] {
  def callback(action: Listener.Action[Event]): Dom.Listener

//  override def callback(action: Listener.Action[Event]): js.Function1[dom.Event, _] = action match {
//    case Action.Pure(event) =>
//      native =>
//        native.preventDefault()
//        unsafeSubmit(event)
//    case Action.Input(event) =>
//      native =>
//        val value = native.target.asInstanceOf[HTMLInputElement].value
//        unsafeSubmit(event(value))
//  }
//
//  def unsafeSubmit(event: Event): Unit =
//    manager
//      .submit(event)
//      .runAsync {
//        case Right(_) => IO.unit
//        case Left(throwable) =>
//          IO {
//            System.err.println("Failed to submit event")
//            throwable.printStackTrace(System.err)
//          }
//      }
//      .unsafeRunSync()
}
