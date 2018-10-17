package com.vngrs.lightningtalk.typeclasses

trait Equals[T] {
  def ===(a: T, b: T): Boolean;

  def =!=(a: T, b: T): Boolean = ! ===(a, b)
}

object Equals extends DefaultEqualsInstances {
  def apply[T](implicit e: Equals[T]): Equals[T] = e

  def from[T](predicate: (T, T) => Boolean): Equals[T] = (a: T, b: T) => predicate(a, b)

  implicit class EqualsSyntax[T](val a: T)(implicit e: Equals[T]) {
    def ===(b: T): Boolean = e.===(a, b)
    def =!=(b: T): Boolean = e.=!=(a, b)
  }

  object Laws {
    def identityLaw[T <: AnyRef : Equals](a: T, b: T): Boolean = (a eq b) && a === a

    def negationLaw[T: Equals](a: T, b: T): Boolean = (a === b) == (! (a =!= b))

    def associativityLaw[T: Equals](a: T, b: T): Boolean = (a === b) === (b === a)
  }
}
