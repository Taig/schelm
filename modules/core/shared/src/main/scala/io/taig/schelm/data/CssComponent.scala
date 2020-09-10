package io.taig.schelm.data

sealed abstract class CssComponent[F[+_], +A] extends Product with Serializable

object CssComponent {
    final case class Styled[F[+_], +A](component: F[Component.Element[A]], style: String) extends CssComponent[F, A]
  final case class Unstyled[F[+_], +A](component: F[Component[A]]) extends CssComponent[F, A]
}

sealed abstract class CrazyReference[F[+_], +A] extends Product with Serializable

object CrazyReference {
  final case class Element[F[+_], +A](component: F[Component.Element[A]], node: String) extends CrazyReference[F, A]
  final case class Fragment[F[+_], +A](component: F[Component.Fragment[A]]) extends CrazyReference[F, A]
}

object Playground {
  final case class CssHtml(css: CrazyReference[CssComponent[Pure, +*], CssHtml])

  val x: CssHtml = CssHtml(CrazyReference.Fragment(CssComponent.Unstyled(Pure(Component.Text("", Listeners.Empty)))))

  x.css match {
    case CrazyReference.Element(component, node) =>
    case CrazyReference.Fragment(component) => component match {
      case CssComponent.Styled(component, style) =>
      case CssComponent.Unstyled(component) => (component)
    }
  }

//  x.css match {
//    case CssComponent.Styled(component, style) =>
//    case CssComponent.Unstyled(component) => component
//  }

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