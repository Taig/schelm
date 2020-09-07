package io.taig.schelm.interpreter

import cats.MonadError
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.{Html, HtmlDiff, Patcher}

final class HtmlPatcher[F[_], Event, Node, Element <: Node](
    val dom: Dom.Aux[F, Event, Node, Element, _],
    renderer: Renderer[F, Html[Event], List[Node]]
)(implicit F: MonadError[F, Throwable])
    extends Patcher[F, List[Node], HtmlDiff[Event]] {
  override def patch(nodes: List[Node], diff: HtmlDiff[Event]): F[Unit] = patch(nodes, diff, cursor = 0)

  def patch(nodes: List[Node], diff: HtmlDiff[Event], cursor: Int): F[Unit] = {
    println(diff)
    diff match {
      case HtmlDiff.AddAttribute(attribute) =>
        for {
          node <- select(nodes, cursor)
          element <- element(node)
          _ <- dom.setAttribute(element, attribute.key.value, attribute.value.value)
        } yield ()
      case HtmlDiff.AppendChild(html) =>
        for {
          parent <- select(nodes, cursor)
          parent <- element(parent)
          children <- renderer.render(html)
          _ <- children.traverse_(dom.appendChild(parent, _))
        } yield ()
      case HtmlDiff.AddListener(listener) =>
        for {
          node <- select(nodes, cursor)
          _ <- dom.addEventListener(node, listener.name.value, dom.callback(listener.action))
        } yield ()
      case HtmlDiff.Clear        => select(nodes, cursor).flatMap(element).flatMap(dom.innerHtml(_, ""))
      case HtmlDiff.Group(diffs) => diffs.traverse_(patch(nodes, _))
      case HtmlDiff.Replace(html) =>
        for {
          node <- select(nodes, cursor)
          parent <- parent(node)
          parent <- element(parent)
          next <- renderer.render(html)
          _ <- next.lastOption match {
            case Some(last) =>
              dom.replaceChild(parent, node, last) *>
                next.init.reverse.foldLeftM(last) { (reference, node) =>
                  dom.insertBefore(parent, node, reference.some).as(node)
                }
            case None => F.unit
          }
        } yield ()
      case HtmlDiff.RemoveAttribute(key) =>
        for {
          node <- select(nodes, cursor)
          element <- element(node)
          _ <- dom.removeAttribute(element, key.value)
        } yield ()
      case HtmlDiff.RemoveChild(index) =>
        for {
          node <- select(nodes, cursor)
          parent <- element(node)
          child <- child(parent, index)
          _ <- dom.removeChild(parent, child)
        } yield ()
      case HtmlDiff.RemoveListener(event) =>
        ???
      case HtmlDiff.UpdateAttribute(key, value) =>
        for {
          node <- select(nodes, cursor)
          element <- element(node)
          _ <- dom.setAttribute(element, key.value, value.value)
        } yield ()
      case HtmlDiff.UpdateChild(index, diff) =>
        for {
          node <- select(nodes, cursor)
          element <- element(node)
          child <- child(element, index)
          _ <- patch(List(child), diff, cursor = 0)
        } yield ()
      case HtmlDiff.UpdateListener(event, action) => ???
      case HtmlDiff.UpdateText(value)             => select(nodes, cursor).flatMap(text).flatMap(dom.data(_, value))
    }
  }

  def select(nodes: List[Node], index: Int): F[Node] =
    nodes.get(index) match {
      case Some(node) => node.pure[F]
      case None       => fail(s"No node at index $index")
    }

  def element(node: Node): F[dom.Element] = dom.element(node).liftTo[F](error("Expected an element node"))

  def text(node: Node): F[dom.Text] = dom.text(node).liftTo[F](error("Expected a text node"))

  def parent(node: Node): F[dom.Node] = dom.parentNode(node).flatMap(_.liftTo[F](error("Node does not have a parent")))

  def child(parent: Element, index: Int): F[dom.Node] =
    dom.childAt(parent, index).flatMap(_.liftTo[F](error(s"No child at index $index")))

  def error(message: String): IllegalStateException = new IllegalStateException(s"$message. DOM out of sync?")

  def fail[A](message: String): F[A] = error(message).raiseError[F, A]
}

object HtmlPatcher {
  def apply[F[_]: MonadError[*[_], Throwable], Event, Node, Element <: Node](
      dom: Dom.Aux[F, Event, Node, Element, _],
      renderer: Renderer[F, Html[Event], List[Node]]
  ): Patcher[F, List[Node], HtmlDiff[Event]] = new HtmlPatcher[F, Event, Node, Element](dom, renderer)
}
