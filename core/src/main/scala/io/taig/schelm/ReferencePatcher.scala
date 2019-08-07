package io.taig.schelm

import cats._
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class ReferencePatcher[F[_], A](
    dom: Dom[F, A],
    renderer: Renderer[F, Html[A], Reference[A]]
)(implicit F: MonadError[F, Throwable])
    extends Patcher[F, Reference[A], HtmlDiff[A]] {
  override def patch(
      reference: Reference[A],
      diff: HtmlDiff[A],
      path: Path
  ): F[Reference[A]] =
    // format: off
    (reference, diff) match {
      case (_, diff: HtmlDiff.Group[A]) => group(reference, diff, path)
      case (reference: Reference.Element[A], diff: HtmlDiff.AddAttribute) => addAttribute(reference, diff)
      case (reference: Reference.Element[A], diff: HtmlDiff.RemoveAttribute) => removeAttribute(reference, diff)
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

  def addAttribute(
      reference: Reference.Element[A],
      diff: HtmlDiff.AddAttribute
  ): F[Reference[A]] = {
    val node = reference.node
    val key = diff.attribute.key

    val update = diff.attribute.value match {
      case Value.Flag(true)  => dom.setAttribute(node, key, "")
      case Value.Flag(false) => F.unit
      case Value.Multiple(values, accumulator) =>
        dom.setAttribute(node, key, values.mkString(accumulator.value))
      case Value.One(value) => dom.setAttribute(node, key, value)
    }

    update *> reference.updateAttributes(_ + diff.attribute).pure[F]
  }

  def removeAttribute(
      reference: Reference.Element[A],
      diff: HtmlDiff.RemoveAttribute
  ): F[Reference[A]] =
    dom.removeAttribute(reference.node, diff.key) *>
      reference.updateAttributes(_ - diff.key).pure[F]

  def group(
      reference: Reference[A],
      diff: HtmlDiff.Group[A],
      path: Path
  ): F[Reference[A]] =
    diff.diffs.foldLeftM(reference)(patch(_, _, path))

  def child(
      children: Children[Reference[A]],
      key: Key
  ): F[Reference[A]] =
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
