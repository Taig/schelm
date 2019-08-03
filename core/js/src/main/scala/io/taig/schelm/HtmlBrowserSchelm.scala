package io.taig.schelm

import cats.effect.ConcurrentEffect
import cats.implicits._
import fs2.Stream

object HtmlBrowserSchelm {
  def start[F[_]: ConcurrentEffect, State, Event, Command](
      container: String,
      initial: State,
      render: State => Html[Event],
      events: EventHandler[State, Event, Command],
      commands: CommandHandler[F, Command, Event],
      subscriptions: Stream[F, Event]
  ): F[Unit] =
    for {
      manager <- EventManager.unbounded[F, Event]
      dom <- BrowserDom(manager)
      container <- dom.getElementById(container).map(_.get)
      renderer = HtmlRenderer(dom)
      schelm = Schelm(
        manager,
        HtmlRenderer(dom),
        ReferenceAttacher(dom),
        HtmlDiffer[Event],
        ReferencePatcher(renderer, dom)
      )
      _ <- schelm.start(
        container,
        initial,
        render,
        events,
        commands,
        subscriptions
      )
    } yield ()
}
