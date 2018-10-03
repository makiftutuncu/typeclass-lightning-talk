package com.vngrs.lightningtalk.typeclasses

trait Monoid[T] {
  def empty: T
  def combine(a: T, b: T): T
}

object Monoid {
  def apply[T](implicit m: Monoid[T]): Monoid[T] = m

  def from[T](emptyT: T)(combineT: (T, T) => T): Monoid[T] = new Monoid[T] {
    override def empty: T               = emptyT
    override def combine(a: T, b: T): T = combineT(a, b)
  }

  def empty[T](implicit m: Monoid[T]): T               = m.empty
  def combine[T](a: T, b: T)(implicit m: Monoid[T]): T = m.combine(a, b)

  implicit class MonoidCombineSyntax[T](val a: T)(implicit m: Monoid[T]) {
    def ~(b: T): T = m.combine(a, b)
  }

  object Laws {
    def identityLaw[T: Monoid](a: T): Boolean = (a ~ empty) == a && (empty ~ a) == a

    def associativityLaw[T: Monoid](a: T, b: T, c: T): Boolean = ((a ~ b) ~ c) == (a ~ (b ~ c))
  }
}
