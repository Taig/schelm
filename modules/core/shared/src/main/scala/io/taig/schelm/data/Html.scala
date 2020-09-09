//package io.taig.schelm.data
//
//import io.taig.schelm.Navigator
//
//final case class Html(component: Component[Html]) extends AnyVal
//
//object Html {
//  implicit val navigator: Navigator[Html, Html] =
//    new Navigator[Html, Html] {
//      override def attributes(html: Html, f: Attributes => Attributes): Html =
//        Html(Navigator[Component[Html], Html].attributes(html.component, f))
//
//      override def listeners(html: Html, f: Listeners => Listeners): Html =
//        Html(Navigator[Component[Html], Html].listeners(html.component, f))
//
//      override def children(html: Html, f: Children[Html] => Children[Html]): Html =
//        Html(Navigator[Component[Html], Html].children(html.component, f))
//    }
//}
