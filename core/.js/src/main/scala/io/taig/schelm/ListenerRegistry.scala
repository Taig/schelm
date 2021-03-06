package io.taig.schelm

import cats.MonadError
import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import org.scalajs.dom

import scala.scalajs.js

final class ListenerRegistry[F[_]](
    registry: Ref[F, Map[(String, Path), js.Function1[dom.Event, _]]]
)(implicit F: MonadError[F, Throwable]) {
  def register(
      name: String,
      path: Path,
      listener: js.Function1[dom.Event, _]
  ): F[Unit] = {
    val key = (name, path)

    registry
      .modify { registry =>
        registry.get(key) match {
          case Some(_) => (registry, false)
          case None    => (registry.updated(key, listener), true)
        }
      }
      .flatMap {
        case true => F.unit
        case false =>
          val message = s"Action already registered for $name: $path"
          F.raiseError(new RuntimeException(message))
      }
  }

  def unregister(name: String, path: Path): F[js.Function1[dom.Event, _]] = {
    val key = (name, path)

    registry
      .modify { registry =>
        registry.get(key) match {
          case Some(listener) => (registry - key, listener.some)
          case None           => (registry, None)
        }
      }
      .flatMap {
        case Some(listener) => listener.pure[F]
        case None =>
          val message = s"No Action registered for $name: $path"
          F.raiseError(new RuntimeException(message))
      }
  }
}

object ListenerRegistry {
  def apply[F[_]: Sync]: F[ListenerRegistry[F]] =
    Ref[F]
      .of(Map.empty[(String, Path), js.Function1[dom.Event, _]])
      .map(new ListenerRegistry[F](_))
}
