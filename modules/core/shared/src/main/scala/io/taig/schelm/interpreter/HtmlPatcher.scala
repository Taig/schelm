package io.taig.schelm.interpreter

import cats.MonadError
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.{Html, HtmlDiff, Patcher}

final class HtmlPatcher[F[_], Event, Node](
    val dom: Dom.Aux[F, Event, Node],
    renderer: Renderer[F, Html[Event], Node]
)(implicit F: MonadError[F, Throwable])
    extends Patcher[F, Node, HtmlDiff[Event]] {
  override def patch(nodes: List[dom.Node], diff: HtmlDiff[Event]): F[Unit] =
    diff match {
      case HtmlDiff.AddAttribute(attribute) => ???
      case HtmlDiff.AppendChild(html) =>
        for {
          parent <- single(nodes)
          parent <- element(parent)
          children <- renderer.render(html)
          _ <- children.traverse_(dom.appendChild(parent, _))
        } yield ()
      case HtmlDiff.AddListener(listener) => ???
      case HtmlDiff.Clear                 => single(nodes).flatMap(element).flatMap(dom.innerHtml(_, ""))
      case HtmlDiff.Group(diffs)          => ???
      case HtmlDiff.Replace(html) =>
        for {
          node <- single(nodes)
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
      case HtmlDiff.RemoveAttribute(key)          => ???
      case HtmlDiff.RemoveChild(key)              => ???
      case HtmlDiff.RemoveListener(event)         => ???
      case HtmlDiff.UpdateAttribute(key, value)   => ???
      case HtmlDiff.UpdateChild(key, diff)        => ???
      case HtmlDiff.UpdateListener(event, action) => ???
      case HtmlDiff.UpdateText(value)             => single(nodes).flatMap(text).flatMap(dom.data(_, value))
    }

  def single(nodes: List[Node]): F[Node] =
    nodes match {
      case head :: Nil => head.pure[F]
      case Nil         => fail("Expected a single node but got none")
      case _           => fail(s"Expected a single node but got ${nodes.length}")
    }

  def element(node: Node): F[dom.Element] = dom.element(node).liftTo[F](error("Expected an element node"))

  def text(node: Node): F[dom.Text] = dom.text(node).liftTo[F](error("Expected a text node"))

  def parent(node: Node): F[dom.Node] = dom.parentNode(node).flatMap(_.liftTo[F](error("Node does not have a parent")))

  def error(message: String): IllegalStateException = new IllegalStateException(s"$message. DOM out of sync?")

  def fail[A](message: String): F[A] = error(message).raiseError[F, A]
}

object HtmlPatcher {
  def apply[F[_]: MonadError[*[_], Throwable], Event, Node](
      dom: Dom.Aux[F, Event, Node],
      renderer: Renderer[F, Html[Event], Node]
  ): Patcher[F, Node, HtmlDiff[Event]] = new HtmlPatcher[F, Event, Node](dom, renderer)
}
