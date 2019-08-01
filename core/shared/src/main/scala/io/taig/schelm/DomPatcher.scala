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
//      case (node, Diff.RemoveChild(key)) =>
//        val children = node.children.get(key).toList.flatMap(_.head)
//        element(node).flatMap(Dom.removeChildren(_, children)) *>
//          node.updateChildren(_.remove(key)).pure[F]
//      case (node, Diff.Replace(key)) => ???
//      case (node, Diff.UpdateText(value)) =>
//        text(node).flatMap(node => F.delay(node.data = value)) *> node.pure[F]
//      case (node, Diff.UpdateAttribute(Attribute(key, value: Value))) =>
//        element(node).flatMap { element =>
//          value.render.fold(Dom.removeAttribute[F](element, key))(
//            Dom.setAttribute[F](element, key, _)
//          )
//        } *> node.updateAttributes(_.updated(key, value)).pure[F]
//      case (node, Diff.UpdateChild(key, diff)) =>
//        node.children
//          .get(key)
//          .traverse(patch(_, diff))
//          .flatMap {
//            case Some(child) =>
//              node.updateChildren(_.updated(key, child)).pure[F]
//            case None =>
//              EffectHelpers.fail[F](s"No child at key $key. Dom out of sync?")
//          }
      case _ =>
        val message = s"Can not patch node $node with diff $diff"
        EffectHelpers.fail[F](message)
    }

  def extract(node: Node[A, B]): F[B] =
    EffectHelpers.get[F, B](node.head, "No node available. Dom out of sync?")
}
