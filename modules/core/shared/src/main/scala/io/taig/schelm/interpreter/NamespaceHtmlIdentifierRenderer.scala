package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Applicative, Eval, Monad}
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._
import io.taig.schelm.implicits._
import io.taig.schelm.util.NodeAccessor

object NamespaceHtmlIdentifierRenderer {
  def apply[F[_]: Monad]: Renderer[F, NamespaceHtml[F], IdentificationLookup[Eval[Html[F]]]] = {
    val EmptyChildren: Map[Identifier, IdentifierTree[Eval[Html[F]]]] = Map.empty

    def toHtml(namespace: NamespaceHtml[F]): Html[F] = namespace.unfix match {
      case namespace: Namespace.Identified[Node[F, NamespaceHtml[F]]] => toHtml(NamespaceHtml(namespace.namespace))
      case namespace: Namespace.Anonymous[Node[F, NamespaceHtml[F]]] =>
        namespace.value match {
          case node: Node.Element[F, NamespaceHtml[F]] =>
            node.variant match {
              case Node.Element.Variant.Normal(children) =>
                Html(node.copy(variant = Node.Element.Variant.Normal(children.map(toHtml))))
              case Node.Element.Variant.Void => Html(node.asInstanceOf[Node[F, Html[F]]])
            }
          case node: Node.Fragment[NamespaceHtml[F]] => Html(node.copy(children = node.children.map(toHtml)))
          case node: Node.Text[F] => Html(node)
        }
    }

    def render(namespace: NamespaceHtml[F]): F[Map[Identifier, IdentifierTree[Eval[Html[F]]]]] = namespace.unfix match {
      case identified: Namespace.Identified[Node[F, NamespaceHtml[F]]] =>
        val identifier = identified.identifier

        identified.namespace match {
          case identified: Namespace.Identified[Node[F, NamespaceHtml[F]]] =>
            render(NamespaceHtml(identified)).map { children =>
              Map(identifier -> IdentifierTree(Eval.later(namespace.html), children))
            }
          case anonymous: Namespace.Anonymous[Node[F, NamespaceHtml[F]]] =>
            flatten(anonymous.value).map(tree => Map(identifier -> tree))
        }
      case _: Namespace.Anonymous[Node[F, NamespaceHtml[F]]] => EmptyChildren.pure[F]
    }

    def flatten(node: Node[F, NamespaceHtml[F]]): F[IdentifierTree[Eval[Html[F]]]] = {
      node match {
        case node: Node.Element[F, NamespaceHtml[F]] =>
          node.variant match {
            case Variant.Normal(children) =>
              children.foldLeftM(IdentifierTree.leaf(Eval.later(toHtml(???)))) { (tree, child) =>
                render(child).map { children =>
                  // TODO error on conflict
                  if(children.isEmpty) tree else tree.copy(children = tree.children ++ children)
                }
              }
            case Variant.Void => IdentifierTree.leaf(Eval.later(toHtml(???))).pure[F]
          }
        case node: Node.Fragment[NamespaceHtml[F]] =>
          node.children.foldLeftM(IdentifierTree.leaf(Eval.later(toHtml(???)))) { (tree, child) =>
            render(child).map { children =>
              // TODO error on conflict
              if(children.isEmpty) tree else tree.copy(children = tree.children ++ children)
            }
          }
        case node: Node.Text[F] => IdentifierTree.leaf(Eval.now(Html(node))).pure[F]
      }
    }

    Kleisli(namespace => render(namespace).map(IdentificationLookup(Eval.later(toHtml(namespace)), _)))
  }
}
