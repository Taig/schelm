package io.taig.schelm

abstract class Patcher[F[_], Component, Node, Diff] {
  def patch(component: Component, node: Node, diff: Diff, path: Path): F[Node]
}
