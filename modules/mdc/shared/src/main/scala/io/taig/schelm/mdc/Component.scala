package io.taig.schelm.mdc

sealed abstract class Component extends Product with Serializable

object Component {
  final case object Chip extends Component
  final case object ChipSet extends Component
}
