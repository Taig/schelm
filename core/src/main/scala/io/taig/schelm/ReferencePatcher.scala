package io.taig.schelm

import cats._
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class ReferencePatcher[F[_], Event](
    dom: Dom[F, Event],
    renderer: Renderer[F, Html[Event], Reference[Event]]
)(implicit F: MonadError[F, Throwable])
    extends Patcher[F, Reference[Event], HtmlDiff[Event]] {
  override def patch(
      reference: Reference[Event],
      diff: HtmlDiff[Event],
      path: Path
  ): F[Reference[Event]] =
    // format: off
    (reference, diff) match {
      case (_, HtmlDiff.Group(diffs)) =>
        diffs.foldLeftM(reference)(patch(_, _, path))
      case (element@Reference.Element(_, node), HtmlDiff.AddAttribute(attribute)) =>
        val key = attribute.key
        val update = attribute.value match {
          case Value.Flag(true) => dom.setAttribute(node, key, "")
          case Value.Flag(false) => F.unit
          case Value.Multiple(values, accumulator) =>
            dom.setAttribute(node, key, values.mkString(accumulator.value))
          case Value.One(value) => dom.setAttribute(node, key, value)
        }
        update *> element.updateAttributes(_ + attribute).pure[F]
      case (element@Reference.Element(_, node), HtmlDiff.RemoveAttribute(key)) =>
        dom.removeAttribute(node, key) *>
        element.updateAttributes(_ - key).pure[F]
      case (element@Reference.Element(_, node), HtmlDiff.RemoveListener(event)) =>
        dom.removeEventListener(node, event, path) *>
        element.updateListeners(_ - event).pure[F]
      case (parent@Reference.Element(component, _), HtmlDiff.UpdateChild(key, diff)) =>
        for {
          reference <- child(component.children, key)
          child <- patch(reference, diff, path / key)
        } yield parent.updateChildren(_.updated(key, child))
      case (element@Reference.Element(_, node), HtmlDiff.UpdateAttribute(key, value)) =>
        val update = value match {
          case Value.Flag(true) => dom.setAttribute(node, key, "")
          case Value.Flag(false) => dom.removeAttribute(node, key)
          case Value.Multiple(values, accumulator) =>
            dom.setAttribute(node, key, values.mkString(accumulator.value))
          case Value.One(value) => dom.setAttribute(node, key, value)
        }
        update *> element.updateAttributes(_.updated(key, value)).pure[F]
      case (element@Reference.Element(_, node), HtmlDiff.UpdateListener(event, action)) =>
        dom.removeEventListener(node, event, path) *>
        dom.addEventListener(node, event, dom.lift(action), path) *>
        element.updateListeners(_.updated(event, action)).pure[F]
      case (parent@Reference.Fragment(component), HtmlDiff.UpdateChild(key, diff)) =>
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
