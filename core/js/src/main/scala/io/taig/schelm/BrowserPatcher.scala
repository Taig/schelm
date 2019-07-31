package io.taig.schelm

import cats.implicits._
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers
import org.scalajs.dom

final class BrowserPatcher[F[_], A](renderer: Renderer[F, A, Node[A, dom.Node]])(
    implicit F: Sync[F]
) extends Patcher[F, A, dom.Node] {
  override def patch(
      node: Node[A, dom.Node],
      diff: Diff[A]
  ): F[Node[A, dom.Node]] =
    (node, diff) match {
      case (node, diff: Diff.AddChild[A]) =>
        for {
          child <- renderer.render(diff.child)
          element <- element(node)
          _ <- Dom.appendAll(element, child.head.toList)
        } yield node.updateChildren(_.append(diff.key, child))
      case (node, Diff.Group(diffs)) => diffs.foldLeftM(node)(patch)
      case (node, Diff.RemoveChild(key)) =>
        val children = node.children.get(key).toList.flatMap(_.head)
        element(node).flatMap(Dom.removeChildren(_, children)) *>
          node.updateChildren(_.remove(key)).pure[F]
      case (node, Diff.Replace(key)) => ???
      case (node, Diff.UpdateText(value)) =>
        text(node).flatMap(node => F.delay(node.data = value)) *> node.pure[F]
      case (node, Diff.UpdateAttribute(Attribute(key, value: Value))) =>
        element(node).flatMap { element =>
          value.render.fold(Dom.removeAttribute[F](element, key))(
            Dom.setAttribute[F](element, key, _)
          )
        } *> node.updateAttributes(_.updated(key, value)).pure[F]
      case (node, Diff.UpdateChild(key, diff)) =>
        node.children
          .get(key)
          .traverse(patch(_, diff))
          .flatMap {
            case Some(child) =>
              node.updateChildren(_.updated(key, child)).pure[F]
            case None =>
              EffectHelpers.fail[F](s"No child at key $key. Dom out of sync?")
          }
      case _ =>
        val message = s"Can not patch node $node with diff $diff"
        EffectHelpers.fail[F](message)
    }

  def element(value: Node[A, dom.Node]): F[dom.Element] = value.head match {
    case Some(element: dom.Element) => element.pure[F]
    case Some(_)                    => EffectHelpers.fail[F]("Not an Element. Dom out of sync?")
    case None                       => EffectHelpers.fail[F]("No Node. Dom out of sync?")
  }

  def text(value: Node[A, dom.Node]): F[dom.Text] = value.head match {
    case Some(text: dom.Text) => text.pure[F]
    case Some(_)              => EffectHelpers.fail[F]("Not a Text. Dom out of sync?")
    case None                 => EffectHelpers.fail[F]("No Node. Dom out of sync?")
  }
}

object BrowserPatcher {
  def apply[F[_]: Sync, A](
      renderer: Renderer[F, A, Node[A, dom.Node]]
  ): Patcher[F, A, dom.Node] = new BrowserPatcher[F, A](renderer)
}
