package io.taig.schelm.css

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import fs2.Stream
import io.taig.schelm._
import io.taig.schelm.internal.EffectHelpers

//cabstract class CssSchelm[F[_], Event, Node] {
//  val dom: Dom[F, Event, Node]
//
//  final def start[State, Command](
//      id: String,
//      initial: State,
//      render: State => Widget[Event],
//      events: EventHandler[State, Event, Command],
//      commands: CommandHandler[F, Command, Event],
//      subscriptions: Stream[F, Event] = Stream.empty
//  )(implicit F: MonadError[F, Throwable]): F[Unit] =
//    dom
//      .getElementById(id)
//      .flatMap(EffectHelpers.get[F](_, s"No element with id $id found"))
//      .flatMap(start(_, initial, render, events, commands, subscriptions))
//
//  def start[State, Command](
//      container: dom.Element,
//      initial: State,
//      render: State => Widget[Event],
//      events: EventHandler[State, Event, Command],
//      commands: CommandHandler[F, Command, Event],
//      subscriptions: Stream[F, Event]
//  ): F[Unit]
//}

//object CssSchelm {
//  val Id = "schelm-css"
//
//  def enable[F[_]: Sync, State, Event, B](
//      globals: Stylesheet,
//      dom: Dom[F, Event, B],
//      render: State => Widget[Event]
//  ): F[State => F[Html[Event]]] =
//    CssRegistry[F].map { registry => state =>
//      val widget = render(state)
//
//      for {
//        _ <- registry.reset
//        html <- Widget.render(widget, registry)
//        stylesheet <- registry.snapshot.map(globals ++ _)
//        style <- getOrCreateStyleElement(dom)
//        _ <- dom.innerHtml(style, s"\n$stylesheet\n")
//      } yield html
//    }
//
//  def getOrCreateStyleElement[F[_]: Sync, A, B](
//      dom: Dom[F, A, B]
//  ): F[dom.Element] =
//    dom.getElementById(Id).flatMap {
//      case Some(element) => element.pure[F]
//      case None          => createStyleElement[F, A, B](dom)
//    }
//
//  def createStyleElement[F[_]: Sync, A, B](
//      dom: Dom[F, A, B]
//  ): F[dom.Element] =
//    for {
//      element <- dom.createElement("style")
//      _ <- dom.setAttribute(element, "id", Id)
//      head <- dom.head
//      _ <- dom.appendChild(head, element)
//    } yield element
//}
