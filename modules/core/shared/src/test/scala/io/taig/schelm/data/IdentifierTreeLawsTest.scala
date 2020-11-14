package io.taig.schelm.data

import cats.kernel.laws.discipline.{EqTests, MonoidTests}
import cats.laws.discipline.{FunctorTests, TraverseTests}
import io.taig.schelm.util.{Cogens, Generators}
import munit.DisciplineSuite
import org.scalacheck.{Arbitrary, Cogen}

final class IdentifierTreeLawsTest extends DisciplineSuite {
  implicit def arbitrary[A: Arbitrary]: Arbitrary[IdentifierTree[A]] = Arbitrary(
    Generators.identifierTree(Arbitrary.arbitrary[A], maxDepth = 10)
  )

  implicit val cogen: Cogen[IdentifierTree[Int]] = Cogens.identifierTree(Cogen[Int])

  checkAll("IdentifierTree", MonoidTests[IdentifierTree[Int]].monoid)
  checkAll("IdentifierTree", FunctorTests[IdentifierTree].functor[Int, Double, String])
  checkAll("IdentifierTree", EqTests[IdentifierTree[Int]].eqv)
}
