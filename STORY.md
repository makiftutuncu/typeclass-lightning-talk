### What are Type Classes?
> Type classes are a programming pa􏰁ttern originati􏰀ng in Haskell (1). They allow us to extend existi􏰀ng libraries with new func􏰀tionality, without using traditi􏰀onal inheritance, and without altering the original library source code.
>
> 1: The word 'class' doesn't strictly mean `class` in the Scala or Java sense

\-\- Scala with Cats / Noel Welsh and Dave Gurnell / underscore

### Road to Type Classes
Let's see an example of `equals`.

```scala
// Perfectly valid equals statement
val validEquals: Boolean = "123" == "123" // true

// Valid, but really? It seems `==` isn't type-safe.
val alsoValidEquals: Boolean = "123" == 123 // false
```

Ideally, in a statically-typed environment, this should be a compile time error. We can make that happen.

Assume that we have following already defined

```scala
class MyData(val index: Int,
             val key: String,
             val value: Double)

val myData1 = new MyData(1, "a", 1.0)
val myData2 = new MyData(1, "b", 1.0)
```

#### First Attempt
```scala
// Let's define our type-safe equals methods.

// For primitive types, it's easy. Now we cannot do `equals("123", 123)`. It won't compile.
def equals(a: Int,    b: Int): Boolean    = a == b
def equals(a: String, b: String): Boolean = a == b
def equals(a: Double, b: Double): Boolean = a == b

// For custom types, we can re-use `equals`.
def equals(a: MyData, b: MyData): Boolean = equals(a.index, b.index) && equals(a.key, b.key) && equals(a.value, b.value)

// This is obviously tedious.
equals(123, 123)      // Yields `true`
// equals(123, "123") // Will not compile
```

#### Second Attempt
```scala
// Let's generalize the behavior then, with a better method name.
trait Equals[T] {
  def ===(a: T, b: T): Boolean
}

// Concrete explicit implementations of `===` behavior
val intEquals: Equals[Int] = new Equals[Int] {
  override def ===(a: Int, b: Int): Boolean = a == b
}

// Easier, more syntactically pleasing versions of the `Equals` instances
val stringEquals: Equals[String] =
  (a: String, b: String) => a == b
val doubleEquals: Equals[Double] =
  (a: Double, b: Double) => a == b
val myDataEquals: Equals[MyData] =
  (a: MyData, b: MyData) =>
    intEquals.===(a.index, b.index) &&
    stringEquals.===(a.key, b.key) &&
    doubleEquals.===(a.value, b.value)

// But how are we going to use these now?

// Valid and type-safe
intEquals.===(123, 123)            // true
myDataEquals.===(myData1, myData2) // false
// Invalid, not even going to compile
// intEquals.===(123, "123")

// Now, let's write a method to generalize the usage.
def ===[T](a: T, b: T, checker: Equals[T]): Boolean =
  checker.===(a, b)

// Better looking
===(123, 123, intEquals)            // true
===(myData1, myData2, myDataEquals) // false
// Again invalid, not even going to compile
// ===(123, "123", intEquals)
```

#### Third Attempt

```scala
// Now, let's get rid of the 3rd argument so we can do something like `===(123, 123)`.

// This one moves `Equals` instance to an implicit parameter group.
// As long as there is an instance in the scope, we don't need to pass it explicitly.
// Therefore, we can do stuff like `====(123, 123)`
def ====[T](a: T, b: T)(implicit equals: Equals[T]): Boolean =
  equals.===(a, b)

// This is another way to define the implicit constraint on type T
// `implicitly[Equals[T]]` brings an implicit instance, if it exists
def =====[T: Equals](a: T, b: T): Boolean =
  implicitly[Equals[T]].===(a, b)

// Let's define some implicit instances then
implicit val int: Equals[Int] =
  (a: Int, b: Int) => a == b
implicit val string: Equals[String] =
  (a: String, b: String) => a == b
implicit val double: Equals[Double] =
  (a: Double, b: Double) => a == b
implicit val myData: Equals[MyData] =
  (a: MyData, b: MyData) =>
    int.===(a.index, b.index) &&
    string.===(a.key, b.key) &&
    double.===(a.value, b.value)

// Now, it's perfectly OK to do following.
// `Equals` instances are implicitly passed for the correct type.
====(123, 123)
====("123", "124")
====(myData1, myData2)
```

