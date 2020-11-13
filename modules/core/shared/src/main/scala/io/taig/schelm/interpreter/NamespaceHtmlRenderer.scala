package io.taig.schelm.interpreter

import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.Renderer
import io.taig.schelm.data.{ListenerTree, Namespace, NamespaceHtml, Node}
import io.taig.schelm.implicits._

object NamespaceHtmlRenderer {
  def apply[F[_]]: Renderer[F, NamespaceHtml[F], ListenerTree[F, ListenerTree.Children[F]]] = {
    Kleisli { namespace =>
      namespace.unfix
        .foldLeftWithIdentification[ListenerTree[F, ListenerTree.Children.Identifiers[F]]](ListenerTree.Empty) {
          (tree, identification, node) =>
            node match {
              case Node.Fragment(children) => ???
              case Node.Text(_, listeners, _) =>
                if (listeners.isEmpty) tree else tree
            }
        }

      ???
    }
  }
}
