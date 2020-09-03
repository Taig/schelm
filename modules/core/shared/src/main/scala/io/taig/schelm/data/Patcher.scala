package io.taig.schelm.data

abstract class Patcher[F[_], Node, Diff] {
  def patch(nodes: List[Node], diff: Diff): F[Unit]
}
