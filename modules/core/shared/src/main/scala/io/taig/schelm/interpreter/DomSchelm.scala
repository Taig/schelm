package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.effect.implicits._
import cats.implicits._
import io.taig.schelm.algebra._
import io.taig.schelm.data.Patcher

final class DomSchelm[F[_]: Concurrent, View, Event, Node, Element, Diff](
    val dom: Dom.Aux[F, Event, Node, Element, _],
    manager: EventManager[F, Event],
    renderer: Renderer[F, View, Node],
    differ: Differ[View, Diff],
    patcher: Patcher[F, Node, Diff]
) extends Schelm[F, View, Event, Element] {
  override def start[State](
      container: Element,
      initial: State,
      render: State => View,
      events: (State, Event) => State
  ): F[Unit] = {
    val view = render(initial)

    for {
      nodes <- renderer.render(view)
      _ <- nodes.traverse_(dom.appendChild(container, _))
      _ <- manager.subscription
        .mapAccumulate((initial, view)) {
          case ((state, previous), event) =>
            val update = events(state, event)

            if (update == state) ((state, previous), none[Diff])
            else {
              val next = render(update)
              ((update, next), differ.diff(previous, next))
            }
        }
        .collect { case (_, Some(diff)) => diff }
        .evalMap(patcher.patch(nodes, _))
        .compile
        .drain
        .start
    } yield ()
  }

  override def markup[State](initial: State, render: State => View): F[String] =
    renderer.render(render(initial)).map(_.map(dom.serialize).mkString("\n"))
}

object DomSchelm {
  def apply[F[_]: Concurrent, View, Event, Node, Element, Diff](
      dom: Dom.Aux[F, Event, Node, Element, _],
      manager: EventManager[F, Event],
      renderer: Renderer[F, View, Node],
      differ: Differ[View, Diff],
      patcher: Patcher[F, Node, Diff]
  ): Schelm[F, View, Event, Element] = new DomSchelm(dom, manager, renderer, differ, patcher)
}
