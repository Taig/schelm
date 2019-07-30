package com.ayendo.schelm

import cats.implicits._
import cats.effect.Sync
import cats.implicits._
import com.ayendo.schelm.internal.EffectHelpers

final class BrowserPatcher[F[_]: Sync, A](renderer: Renderer[F, A, Node[A]])
    extends Patcher[F, A, Node[A]] {
  override def patch(node: Node[A], diff: Diff[A]): F[Node[A]] =
    (node, diff) match {
      case (Node.Element(component, node), diff: Diff.AddChild[A]) =>
        renderer.render(diff.child).flatMap { child =>
          val children = component.children.append(diff.key, child)
          val update = component.copy(children = children)
          Dom.appendAll(node, child.root) *>
            Node.Element(update, node).pure[F].widen
        }
      case (node, Diff.Group(diffs)) => diffs.foldLeftM(node)(patch)
      case (node @ Node.Element(component, element), Diff.RemoveChild(key)) =>
        val children = component.children.get(key).toList.flatMap(_.root)
        Dom.removeChildren(element, children) *>
          node.updateChildren(_.remove(key)).pure[F]
      case (node, Diff.Replace(key)) => ???
      case (node @ Node.Text(_, text), Diff.UpdateText(value)) =>
        Sync[F].delay(text.data = value).as(node)
      case (
          node @ Node.Element(_, element),
          Diff.UpdateAttribute(attribute @ Attribute(key, value: Value))
          ) =>
        value.render.fold(Dom.removeAttribute[F](element, key))(
          Dom.setAttribute[F](element, key, _)
        ) *>
          node.updateAttributes(_ + attribute).pure[F]
      case (Node.Element(component, node), Diff.UpdateChild(key, diff)) =>
        component.children
          .get(key)
          .traverse(patch(_, diff))
          .flatMap {
            case Some(child) =>
              val children = component.children.updated(key, child)
              val update = component.copy(children = children)
              Node.Element(update, node).pure[F].widen
            case None => EffectHelpers.fail[F]("Illegal dom state")
          }
      case _ =>
        val message = s"Can not patch node $node with diff $diff"
        EffectHelpers.fail[F](message)
    }
}

object BrowserPatcher {
  def apply[F[_]: Sync, A](
      renderer: Renderer[F, A, Node[A]]
  ): Patcher[F, A, Node[A]] = new BrowserPatcher[F, A](renderer)
}
