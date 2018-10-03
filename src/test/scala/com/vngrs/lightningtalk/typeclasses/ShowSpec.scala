package com.vngrs.lightningtalk.typeclasses

import org.scalatest.{Matchers, WordSpec}
import com.vngrs.lightningtalk.typeclasses.Show._

class ShowSpec extends WordSpec with Matchers {
  "Show" should {
    "work for type Boolean" in {
      true.show  shouldBe "true"
      false.show shouldBe "false"
    }

    "work for type Byte" in {
      0.show shouldBe "0"
    }

    "work for type Char" in {
      'c'.show shouldBe "c"
    }

    "work for type Short" in {
      1.show shouldBe "1"
    }

    "work for type Int" in {
      2.show shouldBe "2"
    }

    "work for type Long" in {
      3L.show shouldBe "3"
    }

    "work for type Float" in {
      4.5f.show shouldBe "4.5"
    }

    "work for type Double" in {
      6.7.show shouldBe "6.7"
    }

    "work for type String" in {
      "abc".show shouldBe "abc"
    }

    "work for a custom type" in {
      TestData("abc", 1).show shouldBe """{"abc": 1}"""
    }
  }

  case class TestData(key: String, value: Int)

  implicit val testDataShow: Show[TestData] = Show.from { t => s"""{"${t.key}": ${t.value}}""" }
}
