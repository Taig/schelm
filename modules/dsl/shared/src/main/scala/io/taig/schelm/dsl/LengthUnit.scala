package io.taig.schelm.dsl

/** @see https://developer.mozilla.org/en-US/docs/Learn/CSS/Building_blocks/Values_and_units */
sealed abstract class LengthUnit extends Product with Serializable {
  def render: String = this match {
    case LengthUnit.Absolute.Cm(value)   => s"${value}cm"
    case LengthUnit.Absolute.Mm(value)   => s"${value}mm"
    case LengthUnit.Absolute.Q(value)    => s"${value}q"
    case LengthUnit.Absolute.In(value)   => s"${value}in"
    case LengthUnit.Absolute.Pc(value)   => s"${value}pc"
    case LengthUnit.Absolute.Pt(value)   => s"${value}pt"
    case LengthUnit.Absolute.Px(value)   => s"${value}px"
    case LengthUnit.Relative.Em(value)   => s"${value}em"
    case LengthUnit.Relative.Ex(value)   => s"${value}ex"
    case LengthUnit.Relative.Ch(value)   => s"${value}ch"
    case LengthUnit.Relative.Rem(value)  => s"${value}rem"
    case LengthUnit.Relative.Lh(value)   => s"${value}lh"
    case LengthUnit.Relative.Vw(value)   => s"${value}vw"
    case LengthUnit.Relative.Vh(value)   => s"${value}vh"
    case LengthUnit.Relative.Vmin(value) => s"${value}vmin"
    case LengthUnit.Relative.Vmax(value) => s"${value}vmas"
  }
}

object LengthUnit {
  sealed abstract class Absolute extends LengthUnit

  object Absolute {
    final case class Cm(value: Float) extends Absolute
    final case class Mm(value: Float) extends Absolute
    final case class Q(value: Float) extends Absolute
    final case class In(value: Float) extends Absolute
    final case class Pc(value: Float) extends Absolute
    final case class Pt(value: Float) extends Absolute
    final case class Px(value: Float) extends Absolute
  }

  sealed abstract class Relative extends LengthUnit

  object Relative {
    final case class Em(value: Float) extends Relative
    final case class Ex(value: Float) extends Relative
    final case class Ch(value: Float) extends Relative
    final case class Rem(value: Float) extends Relative
    final case class Lh(value: Float) extends Relative
    final case class Vw(value: Float) extends Relative
    final case class Vh(value: Float) extends Relative
    final case class Vmin(value: Float) extends Relative
    final case class Vmax(value: Float) extends Relative
  }

  final class Ops(val value: Float) extends AnyVal {
    def cm: LengthUnit.Absolute = Absolute.Cm(value)
    def mm: LengthUnit.Absolute = Absolute.Mm(value)
    def q: LengthUnit.Absolute = Absolute.Q(value)
    def in: LengthUnit.Absolute = Absolute.In(value)
    def pc: LengthUnit.Absolute = Absolute.Pc(value)
    def pt: LengthUnit.Absolute = Absolute.Pt(value)
    def px: LengthUnit.Absolute = Absolute.Px(value)

    def em: LengthUnit.Relative = Relative.Em(value)
    def ex: LengthUnit.Relative = Relative.Ex(value)
    def ch: LengthUnit.Relative = Relative.Ch(value)
    def rem: LengthUnit.Relative = Relative.Rem(value)
    def lh: LengthUnit.Relative = Relative.Lh(value)
    def vw: LengthUnit.Relative = Relative.Vw(value)
    def vh: LengthUnit.Relative = Relative.Vh(value)
    def vmin: LengthUnit.Relative = Relative.Vmin(value)
    def vmax: LengthUnit.Relative = Relative.Vmax(value)
  }
}
