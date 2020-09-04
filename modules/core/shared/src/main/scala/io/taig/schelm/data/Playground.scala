//package io.taig.schelm.data
//
//import cats.implicits._
//import io.taig.schelm.data.Playground.StyledHtml
//
//object Playground {
//  sealed abstract class Node[+Event, +A] extends Product with Serializable
//
//  final case class Element[+Event, +A](event: Event, children: Children[A]) extends Node[Event, A]
//
//  final case class Text[+Event](value: String, listeners: Listeners[Event]) extends Node[Event, Nothing]
//
//  sealed abstract class StylesheetNode[+A, +B, +Event] extends Product with Serializable
//
//  object StylesheetNode {
//    final case class Styled[A <: Element[Event, B], +B, +Event](element: A, styles: String) extends StylesheetNode[A, B, Event]
//
//    final case class Unstyled[A, +Event](node: A) extends StylesheetNode[Nothing, A, Event]
//  }
//
//  final case class StyledHtml[+F[+_], +Event](node: StylesheetNode[F[StyledHtml[F, Event]], Node[Event, StyledHtml[F, Event]], Event])
//
//  val x: StyledHtml[Element[Nothing, +*], Nothing] = ???
//  x.node match {
//    case StylesheetNode.Styled(element, styles) =>
//      element.children.map(_.node).map {
//        case StylesheetNode.Styled(element, styles) =>
//          ???
//        case StylesheetNode.Unstyled(node) => ???
//      }
//    case StylesheetNode.Unstyled(node) =>
//  }
//
//  sealed abstract class Widget[+A, +Context] extends Product with Serializable
//
//  object Widget {
//    final case class Pure[A](node: A) extends Widget[A, Nothing]
//
//    final case class Render[A, Context](f: Context => Widget[A, Context]) extends Widget[A, Context]
//  }
//
//  final case class StyledWidget[+F[+_], +Event, +Context](widget: Widget[StylesheetNode[F[StyledWidget[F, Event, Context]], Node[Event, StyledWidget[F, Event, Context]], Event], Context])
//
//  val y: StyledWidget[Element[Nothing, +*], Nothing, Nothing] = ???
//  y.widget match {
//    case Widget.Pure(node) =>
//      node match {
//        case StylesheetNode.Styled(element, styles) =>
//        case StylesheetNode.Unstyled(node) =>
//      }
//    case Widget.Render(f) =>
//  }
//
//}
