package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class DomPatcher[F[_]: Sync, A, B](
    renderer: Renderer[F, A, B],
    dom: Dom[F, A, B]
) extends Patcher[F, A, B] {
  override def patch(node: Node[A, B], diff: Diff[A]): F[Node[A, B]] =
    (node, diff) match {
      case (node, diff: Diff.AddChild[A]) =>
        for {
          element <- extract(node).flatMap(dom.element)
          child <- renderer.render(diff.child)
          _ <- dom.appendChildren(element, child.root)
        } yield node.updateChildren(_.append(diff.key, child))
      case (node, Diff.Group(diffs)) => diffs.foldLeftM(node)(patch)
      case (node, Diff.RemoveChild(key)) =>
        extract(node).flatMap(dom.element).flatMap { parent =>
          val children = node.children.get(key).toList.flatMap(_.head)
          dom.removeChildren(parent, children)
        } *> node.updateChildren(_.remove(key)).pure[F]
      case (node, Diff.UpdateText(value)) =>
        extract(node).flatMap(dom.text).flatMap(dom.data(_, value)) *> node
          .pure[F]
      case (node, Diff.UpdateAttribute(Attribute(key, value: Value))) =>
        extract(node).flatMap(dom.element).flatMap { element =>
          value match {
            case Value.Flag(true)  => dom.setAttribute(element, key, "")
            case Value.Flag(false) => dom.removeAttribute(element, key)
            case Value.Multiple(values, accumulator) =>
              dom.setAttribute(element, key, values.mkString(accumulator.value))
            case Value.One(value) => dom.setAttribute(element, key, value)
          }
        } *> node.updateAttributes(_.updated(key, value)).pure[F]
      case (node, Diff.UpdateChild(key, diff)) =>
        EffectHelpers
          .get[F, Node[A, B]](
            node.children.get(key),
            s"No child at key $key. Dom out of sync?"
          )
          .flatMap(patch(_, diff))
      case _ =>
        val message = s"Can not patch node $node with diff $diff"
        EffectHelpers.fail[F](message)
    }

  def extract(node: Node[A, B]): F[B] =
    EffectHelpers.get[F, B](node.head, "No node available. Dom out of sync?")
}
