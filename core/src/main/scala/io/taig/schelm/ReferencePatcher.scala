package io.taig.schelm

import cats._
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class ReferencePatcher[F[_]: MonadError[?[_], Throwable], Event](
    dom: Dom[F, Event],
    renderer: Renderer[F, Html[Event], Reference[Event]]
) extends Patcher[F, Reference[Event], HtmlDiff[Event]] {
  override def patch(
      reference: Reference[Event],
      diff: HtmlDiff[Event],
      path: Path
  ): F[Reference[Event]] =
    // format: off
    (reference, diff) match {
      case (parent@Reference.Element(component, _), HtmlDiff.Select(key, diff)) =>
        for {
          reference <- child(component.children, key)
          child <- patch(reference, diff, path / key)
        } yield parent.updateChildren(_.updated(key, child))
      case (parent@Reference.Fragment(component), HtmlDiff.Select(key, diff)) =>
        for {
          reference <- child(component.children, key)
          child <- patch(reference, diff, path / key)
        } yield parent.updateChildren(_.updated(key, child))
      case (_, HtmlDiff.Replace(html)) => renderer.render(html, path)
      case (reference @ Reference.Text(_, node), HtmlDiff.UpdateText(value)) =>
        dom.data(node, value) *> reference.pure[F].widen
      case _ =>
        val component = Reference.extract(reference)
        EffectHelpers.fail[F](s"Can not apply patch $diff for $component")
    }
    // format: on

  def child(
      children: Children[Reference[Event]],
      key: Key
  ): F[Reference[Event]] =
    EffectHelpers
      .get[F](children.get(key), s"No child for key $key. Dom out of sync?")
}

object ReferencePatcher {
  def apply[F[_]: MonadError[?[_], Throwable], Event](
      dom: Dom[F, Event],
      renderer: Renderer[F, Html[Event], Reference[Event]]
  ): Patcher[F, Reference[Event], HtmlDiff[Event]] =
    new ReferencePatcher[F, Event](dom, renderer)
}
