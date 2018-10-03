package com.vngrs.lightningtalk.typeclasses

trait Show[T] {
  def show(a: T): String
}

object Show extends DefaultShowInstances {
  def apply[T](implicit s: Show[T]): Show[T]   = s
  def from[T](converter: T => String): Show[T] = (a: T) => converter(a)

  def show[T](a: T)(implicit s: Show[T]): String = s.show(a)

  implicit class ShowSyntax[T](val a: T)(implicit s: Show[T]) {
    def show: String = s.show(a)
  }
}
