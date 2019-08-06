package io.taig.schelm

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.internal.EffectHelpers

final class HtmlPatcher[F[_]: Sync, Event, Node](
    renderer: Renderer[F, Html[Event], List[Node]],
    dom: Dom[F, Event, Node]
) extends Patcher[F, Html[Event], List[Node], HtmlDiff[Event]] {
  override def patch(
      component: Html[Event],
      nodes: List[Node],
      diff: HtmlDiff[Event],
      path: Path
  ): F[List[Node]] =
    (component.value, nodes, diff) match {
      case (_, nodes @ List(node), diff: HtmlDiff.AddChild[Event]) =>
        for {
          element <- dom.element(node)
          children <- renderer.render(diff.child, path)
          _ <- dom.appendChildren(element, children)
        } yield nodes
      case (_, nodes, HtmlDiff.Group(diffs)) =>
        diffs.foldLeftM(nodes)(patch(component, _, _, path))
      case (
          _,
          nodes @ List(node),
          HtmlDiff.UpdateAttribute(Attribute(key, value: Value))
          ) =>
        dom.element(node).flatMap { element =>
          value match {
            case Value.Flag(true)  => dom.setAttribute(element, key, "")
            case Value.Flag(false) => dom.removeAttribute(element, key)
            case Value.Multiple(values, accumulator) =>
              dom.setAttribute(element, key, values.mkString(accumulator.value))
            case Value.One(value) => dom.setAttribute(element, key, value)
          }
        } *> nodes.pure[F]
      case (
          component: Component.Element[Html[Event], Event],
          nodes @ List(node),
          HtmlDiff.UpdateChild(key, diff)
          ) =>
        for {
          child <- EffectHelpers.get[F](
            component.children.get(key),
            s"No child for key $key. Dom out of sync?"
          )
          element <- dom.element(node)
          index <- EffectHelpers.get[F](
            component.children.indexOf(key),
            s"Unknown index for key $key. Dom out of sync?"
          )
          node <- dom.childAt(element, index).flatMap { index =>
            EffectHelpers
              .get[F](index, s"No child at index $index. Dom out of sync?")
          }
          _ <- patch(child, List(node), diff, path / key)
        } yield nodes
      case (_, nodes @ List(node), HtmlDiff.UpdateText(value)) =>
        dom.text(node).flatMap(dom.data(_, value)) *> nodes.pure[F]
      case _ =>
        val message = s"Can not apply patch for $diff. Dom out of sync?"
        EffectHelpers.fail[F](message)
    }
//      case (reference, Diff.RemoveChild(key)) =>
//        node(reference).flatMap(dom.element).flatMap { parent =>
//          val children = reference.children.get(key).toList.flatMap(_.head)
//          dom.removeChildren(parent, children)
//        } *> reference.updateChildren(_.remove(key)).pure[F]
//      case (reference, Diff.UpdateText(value)) =>
//        node(reference).flatMap(dom.text).flatMap(dom.data(_, value)) *>
//          reference.setText(value).pure[F]
}

object HtmlPatcher {
  def apply[F[_]: Sync, Event, Node](
      renderer: Renderer[F, Html[Event], List[Node]],
      dom: Dom[F, Event, Node]
  ): Patcher[F, Html[Event], List[Node], HtmlDiff[Event]] =
    new HtmlPatcher(renderer, dom)
}
