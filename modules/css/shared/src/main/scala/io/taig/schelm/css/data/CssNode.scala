package io.taig.schelm.css.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.Navigator
import io.taig.schelm.css.CssNavigator
import io.taig.schelm.data._

sealed abstract class CssNode[+Event, +A] extends Product with Serializable

object CssNode {
  final case class Styled[Event, A](element: Element[Event, A], style: Style) extends CssNode[Event, A]
  final case class Unstyled[Event, A](node: Node[Event, A]) extends CssNode[Event, A]

  implicit def traverse[Event]: Traverse[CssNode[Event, *]] = new Traverse[CssNode[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: CssNode[Event, A])(f: A => G[B]): G[CssNode[Event, B]] =
      fa match {
        case css: Styled[Event, A]   => css.element.traverse(f).map(element => css.copy(element = element))
        case css: Unstyled[Event, A] => css.node.traverse(f).map(node => css.copy(node = node))
      }

    override def foldLeft[A, B](fa: CssNode[Event, A], b: B)(f: (B, A) => B): B =
      fa match {
        case Styled(element, _) => element.foldl(b)(f)
        case Unstyled(node)     => node.foldl(b)(f)
      }

    override def foldRight[A, B](fa: CssNode[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case Styled(element, _) => element.foldr(lb)(f)
        case Unstyled(node)     => node.foldr(lb)(f)
      }
  }

  implicit def navigator[Event, A]: CssNavigator[Event, CssNode[Event, A], A] =
    new CssNavigator[Event, CssNode[Event, A], A] {
      override def attributes(css: CssNode[Event, A], f: Attributes => Attributes): CssNode[Event, A] =
        css match {
          case css: Styled[Event, A] =>
            css.copy(element = Navigator[Event, Element[Event, A], A].attributes(css.element, f))
          case css: Unstyled[Event, A] => css.copy(node = Navigator[Event, Node[Event, A], A].attributes(css.node, f))
        }

      override def listeners(css: CssNode[Event, A], f: Listeners[Event] => Listeners[Event]): CssNode[Event, A] =
        css match {
          case css: Styled[Event, A] =>
            css.copy(element = Navigator[Event, Element[Event, A], A].listeners(css.element, f))
          case css: Unstyled[Event, A] => css.copy(node = Navigator[Event, Node[Event, A], A].listeners(css.node, f))
        }

      override def children(css: CssNode[Event, A], f: Children[A] => Children[A]): CssNode[Event, A] =
        css match {
          case css: Styled[Event, A] =>
            css.copy(element = Navigator[Event, Element[Event, A], A].children(css.element, f))
          case css: Unstyled[Event, A] => css.copy(node = Navigator[Event, Node[Event, A], A].children(css.node, f))
        }

      override def style(css: CssNode[Event, A], f: Style => Style): CssNode[Event, A] =
        css match {
          case css: Styled[Event, A] =>
            val update = f(css.style)
            if (update.isEmpty) Unstyled(css.element) else css.copy(style = f(css.style))
          case css: Unstyled[Event, A] =>
            css.node match {
              case node: Element[Event, A] =>
                val update = f(Style.Empty)
                if (update.isEmpty) css else Styled(node, update)
              case _ => css
            }
        }
    }
}
