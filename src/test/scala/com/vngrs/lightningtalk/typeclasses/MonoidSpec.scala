package com.vngrs.lightningtalk.typeclasses

import org.scalatest.{Matchers, WordSpec}
import com.vngrs.lightningtalk.typeclasses.Monoid._

class MonoidSpec extends WordSpec with Matchers {
  "An integer summation Monoid" should {
    implicit val m: Monoid[Int] = Monoid.from(0)(_ + _)

    "obey identity law" in {
      Monoid.Laws.identityLaw(5) shouldBe true
    }

    "obey associativity law" in {
      Monoid.Laws.associativityLaw(1, 2, 3) shouldBe true
    }

    "return sum of 2 integers" in {
      3 ~ 5 shouldBe 8
    }
  }

  "An integer multiplication Monoid" should {
    implicit val m: Monoid[Int] = Monoid.from(1)(_ * _)

    "obey identity law" in {
      Monoid.Laws.identityLaw(5) shouldBe true
    }

    "obey associativity law" in {
      Monoid.Laws.associativityLaw(1, 2, 3) shouldBe true
    }

    "return multiplication of 2 integers" in {
      3 ~ 5 shouldBe 15
    }
  }

  "A string concatenation Monoid" should {
    implicit val m: Monoid[String] = Monoid.from("")(_ + _)

    "obey identity law" in {
      Monoid.Laws.identityLaw("abc") shouldBe true
    }

    "obey associativity law" in {
      Monoid.Laws.associativityLaw("a", "b", "c") shouldBe true
    }

    "return concatenation of 2 strings" in {
      "a" ~ "b" shouldBe "ab"
    }
  }

  "A custom Monoid" should {
    implicit val m: Monoid[TestData] = Monoid.from(TestData("", 0)) { (a, b) => TestData(a.key + b.key, a.value + b.value) }

    "obey identity law" in {
      Monoid.Laws.identityLaw(TestData("a", 1)) shouldBe true
    }

    "obey associativity law" in {
      Monoid.Laws.associativityLaw(TestData("a", 1), TestData("b", 2), TestData("c", 3)) shouldBe true
    }

    "return a correct result" in {
      TestData("a", 1) ~ TestData("b", 2) shouldBe TestData("ab", 3)
    }
  }

  "Playing with implicit expansions" should {
    "work correctly for Option and Tuple2" in {
      implicit val intSumMonoid: Monoid[Int]          = Monoid.from(0)(_ + _)
      implicit val stringConcatMonoid: Monoid[String] = Monoid.from("")(_ + _)

      implicit def tuple2Monoid[A: Monoid, B: Monoid]: Monoid[(A, B)] =
        Monoid.from(Monoid.empty[A] -> Monoid.empty[B]) { case ((a1, b1), (a2, b2)) =>
          (a1 ~ a2) -> (b1 ~ b2)
        }

      implicit def optionMonoid[T: Monoid]: Monoid[Option[T]] =
        Monoid.from(Option.empty[T]) {
          case (Some(a), Some(b)) => Some(a ~ b)
          case (Some(a), None)    => Some(a)
          case (None, Some(b))    => Some(b)
          case (None, None)       => None
        }

      val x: Option[(String, Int)] = None
      val y: Option[(String, Int)] = Some("a" -> 5)
      val z: Option[(String, Int)] = Some("b" -> 3)

      val result: Option[(String, Int)] = x ~ y ~ z

      result shouldBe Some("ab" -> 8)
    }

    "work correctly for List and String" in {
      implicit val stringConcatMonoid: Monoid[String] = Monoid.from("")(_ + _)

      implicit def listZippingMonoid[T: Monoid]: Monoid[List[T]] = Monoid.from(List.empty[T]) { (list1, list2) =>
        (list1 zip list2).map { case (t1, t2) => t1 ~ t2 }
      }

      val x: List[String] = List("a", "b")
      val y: List[String] = List("c", "d")

      val result: List[String] = x ~ y

      result shouldBe List("ac", "bd")
    }

    "find minimum and maximum of a list as a tuple" in {
      val minMonoid: Monoid[Int] = Monoid.from(Int.MaxValue) { (a, b) => if (a < b) a else b }
      val maxMonoid: Monoid[Int] = Monoid.from(Int.MinValue) { (a, b) => if (a > b) a else b }

      val minMaxMonoid: Monoid[(Int, Int)] =
        Monoid.from(minMonoid.empty -> maxMonoid.empty) { case ((a1, a2), (b1, b2)) =>
          minMonoid.combine(a1, a2) -> maxMonoid.combine(b1, b2)
        }

      val list = List(1, 287, -3, 689, 1236, -48, 97, -2, 389, 47, 928, -34, 345)

      val (min, max) = list.foldLeft(minMaxMonoid.empty) { case ((currentMin, currentMax), current) =>
        minMaxMonoid.combine(currentMin -> current, currentMax -> current)
      }

      min shouldBe -48
      max shouldBe 1236
    }
  }

  case class TestData(key: String, value: Int)
}
