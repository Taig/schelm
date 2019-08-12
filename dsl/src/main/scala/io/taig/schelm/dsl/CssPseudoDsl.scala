package io.taig.schelm.dsl

import io.taig.schelm.css.{
  Declaration,
  Declarations,
  Modifier,
  PseudoDeclaration
}

trait CssPseudoDsl {
  final def pseudo(
      modifier: String
  )(declarations: Declaration*): PseudoDeclaration =
    PseudoDeclaration(Modifier(modifier), Declarations.from(declarations))

  final def after(declarations: Declaration*): PseudoDeclaration =
    pseudo("::after")(declarations: _*)

  final def active(declarations: Declaration*): PseudoDeclaration =
    pseudo(":active")(declarations: _*)

  final def before(declarations: Declaration*): PseudoDeclaration =
    pseudo("::before")(declarations: _*)

  final def checked(declarations: Declaration*): PseudoDeclaration =
    pseudo(":checked")(declarations: _*)

  final def default(declarations: Declaration*): PseudoDeclaration =
    pseudo(":default")(declarations: _*)

  final def dir(
      direction: String
  )(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":dir($direction)")(declarations: _*)

  final def disabled(declarations: Declaration*): PseudoDeclaration =
    pseudo(":disabled")(declarations: _*)

  final def empty(declarations: Declaration*): PseudoDeclaration =
    pseudo(":empty")(declarations: _*)

  final def enabled(declarations: Declaration*): PseudoDeclaration =
    pseudo(":enabled")(declarations: _*)

  final def first(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first")(declarations: _*)

  final def firstChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first-child")(declarations: _*)

  final def firstOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":first-of-type")(declarations: _*)

  final def fullscreen(declarations: Declaration*): PseudoDeclaration =
    pseudo(":fullscreen")(declarations: _*)

  final def focus(declarations: Declaration*): PseudoDeclaration =
    pseudo(":focus")(declarations: _*)

  final def hover(declarations: Declaration*): PseudoDeclaration =
    pseudo(":hover")(declarations: _*)

  final def indeterminate(declarations: Declaration*): PseudoDeclaration =
    pseudo(":indeterminate")(declarations: _*)

  final def inRange(declarations: Declaration*): PseudoDeclaration =
    pseudo(":in-range")(declarations: _*)

  final def invalid(declarations: Declaration*): PseudoDeclaration =
    pseudo(":invalid")(declarations: _*)

  final def lang(code: String)(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":lang($code)")(declarations: _*)

  final def lastChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":last-child")(declarations: _*)

  final def lastOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":last-of-type")(declarations: _*)

  final def not(
      selector: String
  )(declarations: Declaration*): PseudoDeclaration =
    pseudo(s":not($selector)")(declarations: _*)

  final def nthChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-child")(declarations: _*)

  final def nthLastChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-last-child")(declarations: _*)

  final def nthLastOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-last-of-type")(declarations: _*)

  final def nthOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":nth-of-type")(declarations: _*)

  final def onlyChild(declarations: Declaration*): PseudoDeclaration =
    pseudo(":only-child")(declarations: _*)

  final def onlyOfType(declarations: Declaration*): PseudoDeclaration =
    pseudo(":only-of-type")(declarations: _*)

  final def optional(declarations: Declaration*): PseudoDeclaration =
    pseudo(":optional")(declarations: _*)

  final def outOfRange(declarations: Declaration*): PseudoDeclaration =
    pseudo(":out-of-range")(declarations: _*)

  final def required(declarations: Declaration*): PseudoDeclaration =
    pseudo(":required")(declarations: _*)

  final def visited(declarations: Declaration*): PseudoDeclaration =
    pseudo(":visited")(declarations: _*)
}
