package io.taig.schelm.dsl

import io.taig.schelm.css.{
  Declaration,
  Declarations,
  Modifier,
  PseudoDeclaration
}

trait CssPseudoDsl {
  def pseudo(
      modifier: String
  )(declarations: Declaration*): PseudoDeclaration =
    PseudoDeclaration(Modifier(modifier), Declarations.from(declarations))
  def after(declarations: Declaration*): PseudoDeclaration =
    pseudo("::after")(declarations: _*)

  def active(declarations: Declaration*): PseudoDeclaration =
    pseudo(":active")(declarations: _*)

  def before(declarations: Declaration*): PseudoDeclaration =
    pseudo("::before")(declarations: _*)

  def checked(declarations: Declaration*): PseudoDeclaration =
    pseudo(":checked")(declarations: _*)

  def default(declarations: Declaration*): PseudoDeclaration =
    pseudo(":default")(declarations: _*)

  def dir(direction: String)(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":dir($direction)")(declarations: _*)

  def disabled(declarations: Declaration*): PseudoDeclaration =
    pseudo(":disabled")(declarations: _*)

  def empty(declarations: Declaration*): PseudoDeclaration =
    pseudo(":empty")(declarations: _*)

  def enabled(declarations: Declaration*): PseudoDeclaration =
    pseudo(":enabled")(declarations: _*)

  def first(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first")(declarations: _*)

  def firstChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first-child")(declarations: _*)

  def firstOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first-of-type")(declarations: _*)

  def fullscreen(declarations: Declaration*): PseudoDeclaration =
    pseudo(":fullscreen")(declarations: _*)

  def focus(declarations: Declaration*): PseudoDeclaration =
    pseudo(":focus")(declarations: _*)

  def hover(declarations: Declaration*): PseudoDeclaration =
    pseudo(":hover")(declarations: _*)

  def indeterminate(declarations: Declaration*): PseudoDeclaration =
    pseudo(":indeterminate")(declarations: _*)

  def inRange(declarations: Declaration*): PseudoDeclaration =
    pseudo(":in-range")(declarations: _*)

  def invalid(declarations: Declaration*): PseudoDeclaration =
    pseudo(":invalid")(declarations: _*)

  def lang(code: String)(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":lang($code)")(declarations: _*)

  def lastChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":last-child")(declarations: _*)

  def lastOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":last-of-type")(declarations: _*)

  def not(selector: String)(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":not($selector)")(declarations: _*)

  def nthChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-child")(declarations: _*)

  def nthLastChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-last-child")(declarations: _*)

  def nthLastOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-last-of-type")(declarations: _*)

  def nthOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-of-type")(declarations: _*)

  def onlyChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":only-child")(declarations: _*)

  def onlyOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":only-of-type")(declarations: _*)

  def optional(declarations: Declaration*): PseudoDeclaration =
    pseudo(":optional")(declarations: _*)

  def outOfRange(declarations: Declaration*): PseudoDeclaration =
    pseudo(":out-of-range")(declarations: _*)

  def required(declarations: Declaration*): PseudoDeclaration =
    pseudo(":required")(declarations: _*)

  def visited(declarations: Declaration*): PseudoDeclaration =
    pseudo(":visited")(declarations: _*)
}
