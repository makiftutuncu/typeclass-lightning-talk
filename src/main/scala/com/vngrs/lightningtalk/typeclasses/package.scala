package com.vngrs.lightningtalk

package object typeclasses {
  trait DefaultEqualsInstances {
    implicit val booleanEqualsInstance: Equals[Boolean] = Equals.from(_ == _)
    implicit val byteEqualsInstance: Equals[Byte]       = Equals.from(_ == _)
    implicit val charEqualsInstance: Equals[Char]       = Equals.from(_ == _)
    implicit val shortEqualsInstance: Equals[Short]     = Equals.from(_ == _)
    implicit val intEqualsInstance: Equals[Int]         = Equals.from(_ == _)
    implicit val longEqualsInstance: Equals[Long]       = Equals.from(_ == _)
    implicit val floatEqualsInstance: Equals[Float]     = Equals.from(_ == _)
    implicit val doubleEqualsInstance: Equals[Double]   = Equals.from(_ == _)
    implicit val stringEqualsInstance: Equals[String]   = Equals.from(_ == _)
  }

  trait DefaultShowInstances {
    implicit val booleanShowInstance: Show[Boolean] = Show.from(_.toString)
    implicit val byteShowInstance: Show[Byte]       = Show.from(_.toString)
    implicit val charShowInstance: Show[Char]       = Show.from(_.toString)
    implicit val shortShowInstance: Show[Short]     = Show.from(_.toString)
    implicit val intShowInstance: Show[Int]         = Show.from(_.toString)
    implicit val longShowInstance: Show[Long]       = Show.from(_.toString)
    implicit val floatShowInstance: Show[Float]     = Show.from(_.toString)
    implicit val doubleShowInstance: Show[Double]   = Show.from(_.toString)
    implicit val stringShowInstance: Show[String]   = Show.from(identity)
  }
}
