package io.taig.schelm.data

sealed abstract class CssComponent[F[+_], +A] extends Product with Serializable

object CssComponent {
    final case class Styled[F[+_], +A <: Component.Element[F[A], Component.Element.Type[F[A]]]](node: F[A], style: String) extends CssComponent[F, A]
  final case class Unstyled[F[+_], +A <: Component[F[A]]](component: F[A]) extends CssComponent[F, A]
}

//sealed abstract class CrazyReference[F[_], A <: Component[F[A]]] extends Product with Serializable
//
//object CrazyReference {
//  final case class Element[F[_], A <: Component.Element[F[A]]](component: F[A], node: String) extends CrazyReference[F, A]
//  final case class Fragment[F[_], A <: Component.Fragment[F[A]]](component: F[A]) extends CrazyReference[F, A]
//  final case class Text[F[_]](component: F[Component.Text], node: String) extends CrazyReference[F, Component.Text]
//}

object Playground {
  final case class CssHtml(css: CssComponent[Pure, Component[CssHtml]])

  val x: CssHtml = CssHtml(CssComponent.Styled(Pure(Component.Element(???, ???)), ""))

  x.css match {
    case css: CssComponent.Unstyled[Pure, Component[CssHtml]] => (css.component: Component[CssHtml]) match {
      case Component.Element(tag, tpe) =>
      case Component.Fragment(children) => children.indexed.map(???)
      case Component.Text(value, listeners) =>
    }
  }

//
//  x.css match {
//    case CssComponent.Styled(node, style) => (node: Component[CssHtml]) match {
//      case Component.Element(tag, tpe) =>
//      case Component.Fragment(children) => children.indexed.map(???)
//      case Component.Text(value, listeners) =>
//    }
//  }


//  x.css match {
//    case CssComponent.Styled(component, style) =>
//    case CssComponent.Unstyled(component, style) =>
//      (component: Component[CssHtml]) match {
//        case Component.Element(tag, tpe) =>
//        case Component.Fragment(children) => children.indexed.map(_.css)
//        case Component.Text(value, listeners) =>
//      }
//  }
}