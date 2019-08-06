package io.taig.schelm

import cats._
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class ReferencePatcher[F[_]: MonadError[?[_], Throwable], Event](
    renderer: Renderer[F, Html[Event], Reference[Event]]
) extends Patcher[F, Reference[Event], HtmlDiff[Event]] {
  override def patch(
      reference: Reference[Event],
      diff: HtmlDiff[Event],
      path: Path
  ): F[Reference[Event]] = {
    (reference, diff) match {
      case (Reference.Element(component, _), HtmlDiff.Select(key, diff)) =>
        for {
          reference <- child(component.children, key)
          child <- patch(reference, diff, path / key)
        } yield reference.updateChildren(_.updated(key, child))
      case (Reference.Fragment(component), HtmlDiff.Select(key, diff)) =>
        for {
          reference <- child(component.children, key)
          child <- patch(reference, diff, path / key)
        } yield reference.updateChildren(_.updated(key, child))
      case (_, HtmlDiff.Replace(html)) => renderer.render(html, path)
      case _                           => EffectHelpers.fail[F](s"Can not apply patch for $diff")
    }
  }

  def child(
      children: Children[Reference[Event]],
      key: Key
  ): F[Reference[Event]] =
    EffectHelpers
      .get[F](children.get(key), s"No child for key $key. Dom out of sync?")
}

object ReferencePatcher {
  def apply[F[_]: MonadError[?[_], Throwable], Event](
      renderer: Renderer[F, Html[Event], Reference[Event]]
  ): Patcher[F, Reference[Event], HtmlDiff[Event]] =
    new ReferencePatcher[F, Event](renderer)
}
