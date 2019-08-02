package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class DomPatcher[F[_]: Sync, Event, Node](
    renderer: Renderer[F, Event, Node],
    dom: Dom[F, Event, Node]
) extends Patcher[F, Event, Node] {
  override def patch(
      reference: Reference[Event, Node],
      diff: Diff[Event]
  ): F[Reference[Event, Node]] =
    (reference, diff) match {
      case (reference, diff: Diff.AddChild[Event]) =>
        for {
          element <- node(reference).flatMap(dom.element)
          child <- renderer.render(diff.child)
          _ <- dom.appendChildren(element, child.root)
        } yield reference.updateChildren(_.append(diff.key, child))
      case (reference, Diff.Group(diffs)) => diffs.foldLeftM(reference)(patch)
      case (reference, Diff.RemoveChild(key)) =>
        node(reference).flatMap(dom.element).flatMap { parent =>
          val children = reference.children.get(key).toList.flatMap(_.head)
          dom.removeChildren(parent, children)
        } *> reference.updateChildren(_.remove(key)).pure[F]
      case (reference, Diff.UpdateText(value)) =>
        node(reference).flatMap(dom.text).flatMap(dom.data(_, value)) *>
          reference.setText(value).pure[F]
      case (reference, Diff.UpdateAttribute(Attribute(key, value: Value))) =>
        node(reference).flatMap(dom.element).flatMap { element =>
          value match {
            case Value.Flag(true)  => dom.setAttribute(element, key, "")
            case Value.Flag(false) => dom.removeAttribute(element, key)
            case Value.Multiple(values, accumulator) =>
              dom.setAttribute(element, key, values.mkString(accumulator.value))
            case Value.One(value) => dom.setAttribute(element, key, value)
          }
        } *> reference.updateAttributes(_.updated(key, value)).pure[F]
      case (reference, Diff.UpdateChild(key, diff)) =>
        EffectHelpers
          .get[F](
            reference.children.get(key),
            s"No child at key $key. Dom out of sync?"
          )
          .flatMap(patch(_, diff))
          .map(child => reference.updateChildren(_.updated(key, child)))
      case _ =>
        val message = s"Can not patch node $reference with diff $diff"
        EffectHelpers.fail[F](message)
    }

  def node(reference: Reference[Event, Node]): F[Node] =
    EffectHelpers.get[F](reference.head, "No node available. Dom out of sync?")
}

object DomPatcher {
  def apply[F[_]: Sync, A, B](
      renderer: Renderer[F, A, B],
      dom: Dom[F, A, B]
  ): Patcher[F, A, B] = new DomPatcher[F, A, B](renderer, dom)
}
