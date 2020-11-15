package io.taig.schelm

import cats.{Comonad, Foldable}
import cats.effect.IO
import cats.implicits._
import cats.implicits.none
import io.taig.schelm.data.{Attributes, Children, Fix, Html, Identifier, Listeners, Namespace, NamespaceHtml, Node}
import io.taig.schelm.util.NodeAccessor
import io.taig.schelm.implicits._

object Playground {
  implicit def nested[G[_], F[_[_], _]]: NodeAccessor[λ[(H[_], A) => G[F[H, A]]]] = ???

  implicit def namespaceInstance[F[_[_], _]]: NodeAccessor[Lambda[(H[_], A) => Namespace[F[H, A]]]] = ???

  val x: Namespace[Node[IO, NamespaceHtml[IO]]] = ???
  nested[Namespace, Node].children(x)
  //toNodeAccessorOps[Lambda[(G[_], A) => Namespace[Node[G, A]]], IO, NamespaceHtml[IO]](x)

  def test[A](value: A)(implicit na: NodeAccessor[Lambda[(F[_], B) => A]])


}
