package com.vngrs.lightningtalk.typeclasses

import com.vngrs.lightningtalk.typeclasses.Equals._
import org.scalatest.{Matchers, WordSpec}

class EqualsSpec extends WordSpec with Matchers {
  "Equals" should {
    "obey identity law" in {
      val s1: String = "abc"
      val s2: String = "def"

      Equals.Laws.identityLaw(s1, s1) shouldBe true
      Equals.Laws.identityLaw(s1, s2) shouldBe false
    }

    "obey negation law" in {
      val s1: String = "abc"
      val s2: String = "def"

      Equals.Laws.negationLaw(s1, s2) shouldBe true
    }

    "obey associativity law" in {
      val s1: String = "abc"
      val s2: String = "def"

      Equals.Laws.associativityLaw(s1, s2) shouldBe Equals.Laws.associativityLaw(s2, s1)
    }

    "work for type Boolean" in {
      val b1: Boolean = true
      val b2: Boolean = true
      val b3: Boolean = false

      (b1 === b2) shouldBe true
      (b1 === b3) shouldBe false
    }

    "work for type Byte" in {
      val b1: Byte = 0
      val b2: Byte = 0
      val b3: Byte = 1

      (b1 === b2) shouldBe true
      (b1 === b3) shouldBe false
    }

    "work for type Char" in {
      val c1: Char = 'a'
      val c2: Char = 'a'
      val c3: Char = 'b'

      (c1 === c2) shouldBe true
      (c1 === c3) shouldBe false
    }

    "work for type Short" in {
      val s1: Short = 0
      val s2: Short = 0
      val s3: Short = 1

      (s1 === s2) shouldBe true
      (s1 === s3) shouldBe false
    }

    "work for type Int" in {
      val i1: Int = 0
      val i2: Int = 0
      val i3: Int = 1

      (i1 === i2) shouldBe true
      (i1 === i3) shouldBe false
    }

    "work for type Long" in {
      val l1: Long = 0L
      val l2: Long = 0L
      val l3: Long = 1L

      (l1 === l2) shouldBe true
      (l1 === l3) shouldBe false
    }

    "work for type Float" in {
      val f1: Float = 0.0f
      val f2: Float = 0.0f
      val f3: Float = 1.0f

      (f1 === f2) shouldBe true
      (f1 === f3) shouldBe false
    }

    "work for type Double" in {
      val d1: Double = 0.0
      val d2: Double = 0.0
      val d3: Double = 1.0

      (d1 === d2) shouldBe true
      (d1 === d3) shouldBe false
    }

    "work for type String" in {
      val s1: String = "abc"
      val s2: String = "abc"
      val s3: String = "def"

      (s1 === s2) shouldBe true
      (s1 === s3) shouldBe false
    }

    "work for a custom type" in {
      val t1: TestData = new TestData("abc")
      val t2: TestData = new TestData("abc")
      val t3: TestData = new TestData("def")

      (t1 === t2) shouldBe true
      (t1 === t3) shouldBe false
    }
  }

  // We override this and mark as non-implicit because scalatest already has a `===` as implicit in the scope. :)
  override def convertToEqualizer[T](left: T): Equalizer[T] = super.convertToEqualizer(left)

  // Intentionally not a case class because we don't want generated `equals` method, we're trying to test our own `Equals`.
  class TestData(val data: String)

  implicit val testDataEquals: Equals[TestData] = Equals.from { (t1, t2) =>
    t1.data === t2.data
  }
}
