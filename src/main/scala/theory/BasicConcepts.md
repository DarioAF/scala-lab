# Basic Concepts

## Immutability
> In Scala, immutability refers to the property of objects that cannot be modified after they are created. 
> Once an immutable object is created, its state cannot change, and any attempt to modify it will result in a new object being created with the updated values. 
> Immutability ensures that data remains constant and cannot be accidentally altered, which has several benefits in concurrent programming.

Benefits of immutability in concurrent programming:
1) **Thread Safety:** Since immutable objects cannot be modified, they are inherently thread-safe. Multiple threads can access and use immutable objects without the risk of concurrent modifications and data corruption.
2) **No Synchronization:** Immutability eliminates the need for explicit synchronization mechanisms (e.g., locks) to manage concurrent access to shared data. This simplifies the code and reduces the likelihood of deadlocks and race conditions.
3) **Predictable Behavior:** Immutable objects have a predictable state throughout their lifecycle, making it easier to reason about their behavior and ensuring consistent results in concurrent scenarios.

### Example:

```scala
case class Person(name: String, age: Int)

// Create an immutable instance of Person
val alice = Person("Alice", 30)

// Note that this reassignment of val is forbidden in Scala
alice.age = 31

// To work with vals, we need to create a new instance with the updated age
val updatedAlice = person.copy(age = 31)

println(s"Original Person: $alice")
println(s"Person after birthday: $updatedAlice")
```
In this example, we define a `Person` case class with two fields: `name` and `age`. 
The `Person` instance is created with the `val` keyword, making it immutable. 
To modify the `age` field, we need to do it using the `copy` method, it returns a new instance of `Person` with the updated age. 
The original person object remains unchanged. 
This behavior ensures that the state of the `Person` object remains constant, enforcing immutability in Scala.

## Option
> In Scala, Option is a container type that represents an optional value that may or may not exist. 
> It is used as a safer alternative to handling null values and helps in avoiding null pointer exceptions. 
> An Option can either be Some(value) if the value is present or None if the value is absent.

Benefits of Option in handling null or absent values:
1) **Null Safety:** Option provides a clear and explicit way to handle the possibility of null values without introducing null pointer exceptions.
2) **Avoiding NullPointerExceptions:** By using Option, developers are encouraged to handle the absence of a value explicitly, reducing the likelihood of unintended null dereferences.

### Example:

```scala
// Function to find an element in a list and return it as an Option
def findElement(list: List[Int], target: Int): Option[Int] = 
  list.find(_ == target)

val numbers = List(1, 2, 3, 4, 5)

val targetValue = 3
val foundElement = findElement(numbers, targetValue)

foundElement match {
  case Some(value) => println(s"Element $targetValue, found: $value")
  case None => println(s"Element $targetValue, not found.")
}
```

In this example, the function `findElement` takes a list of integers and a target value as input. 
It uses the `find` method on the list to look for the target value. 
If the value is found, it returns `Some(value)` wrapped in an `Option`. If the value is not found, it returns `None`. 
The function demonstrates how `Option` can be used to handle the absence of a value safely.

## Pattern matching
> Pattern matching is a powerful feature in Scala that allows you to match the structure of data against predefined patterns and extract values accordingly. 
> It is widely used in functional programming to handle different cases or scenarios elegantly and concisely.

Significance of pattern matching in functional programming:
1) **Concise Control Flow:** Pattern matching allows you to express complex control flows in a more concise and readable manner compared to traditional if-else or switch statements.
2) **Extensibility:** Pattern matching is extensible, meaning you can define your custom pattern matching rules by implementing the unapply method in extractor objects. This gives you the flexibility to pattern match on your own data types.
3) **Exhaustiveness Checking:** The Scala compiler performs exhaustiveness checking on pattern matching, ensuring that all possible cases are covered. This helps catch potential bugs and makes the code more robust.

### Example

```scala
def matchDayOfWeek(day: String): String = day match {
  case "Monday" | "Tuesday" | "Wednesday" | "Thursday" | "Friday" => "Weekday"
  case "Saturday" | "Sunday" => "Weekend"
  case _ => "Invalid day"
}

val day1 = "Monday"
val day2 = "Saturday"
val day3 = "InvalidDay"

println(s"$day1 is a ${matchDayOfWeek(day1)}")
println(s"$day2 is a ${matchDayOfWeek(day2)}")
println(s"$day3 is a ${matchDayOfWeek(day3)}")
```

In this example, the `matchDayOfWeek` function takes a day as input and uses pattern matching to classify it as a weekday, weekend, or an invalid day. 
The first case matches any of the weekdays, the second case matches weekends, and the third case is the default (catch-all) case for any other input. 
The use of pattern matching makes the code concise and easy to understand.

## Currying
> Currying is a technique in functional programming where a function that takes multiple arguments is transformed into a series of functions, each taking a single argument. 
> It allows for partial application of functions and creates a more modular and flexible codebase.

Benefits of currying in functional programming:
1) **Partial Function Application:** Currying enables partial function application, where you can fix some arguments of a function and obtain a new function with the remaining arguments. This makes it easier to reuse and compose functions.
2) **Function Composition:** Currying promotes function composition, where functions can be combined to create more complex and reusable operations.
3) **Flexibility and Reusability:** Currying provides a higher level of flexibility, allowing functions to be reused in different contexts with varying numbers of arguments.

### Example

```scala
// Uncurried function
def add(x: Int, y: Int): Int = x + y

// Curried function
def curriedAdd(x: Int)(y: Int): Int = x + y

// Partial application of curriedAdd
val addFive = curriedAdd(5) _

println(s"Uncurried add: ${add(3, 4)}")
println(s"Curried add: ${curriedAdd(3)(4)}")
println(s"Partial application of curriedAdd: ${addFive(4)}")
```

In this example, we define an `add` function that takes two arguments and returns their sum. 
We then define a `curriedAdd` function, which is the curried version of the `add` function. 
The curried version allows us to call it with one argument to get a new function that expects the second argument.

The partial application of the `curriedAdd` function is demonstrated using `addFive`, which is a new function derived from `curriedAdd` with `x` fixed as 5. 
This partial function application creates a reusable function that only requires one argument to complete the calculation.

## Higher-Order Functions
> Higher-order functions in Scala are functions that take one or more functions as arguments or return functions as results. 
> They treat functions as first-class citizens, allowing them to be manipulated and composed like any other data type. 
> Higher-order functions play a significant role in functional programming, enabling more concise and expressive code.

Significance of higher-order functions in functional programming:
1) **Abstraction:** Higher-order functions abstract common patterns of computation, making the code more modular and reusable.
2) **Function Composition:** Higher-order functions enable function composition, where multiple functions can be combined to create new functions that perform complex operations.
3) **Encapsulation of Behavior:** Higher-order functions encapsulate behavior, allowing different behaviors to be passed to the same function, making it flexible and adaptable.

### Example

```scala
// Higher-order function: map
def doubleNumbers(numbers: List[Int], f: Int => Int): List[Int] = numbers.map(f)

// Function to double a number
def double(x: Int): Int = x * 2

// Function to square a number
def square(x: Int): Int = x * x

val numbers = List(1, 2, 3, 4, 5)

val doubledNumbers = doubleNumbers(numbers, double)
val squaredNumbers = doubleNumbers(numbers, square)

println(s"Doubled numbers: $doubledNumbers")
println(s"Squared numbers: $squaredNumbers")
```

In this example, we define a higher-order function `doubleNumbers` that takes a list of numbers and a function `f` as arguments. 
The `map` function is used inside `doubleNumbers` to apply `f` to each element of the list, effectively doubling or squaring the numbers.

We define two simple functions `double` and `square` that double and square a given number, respectively. 
By passing these functions as arguments to `doubleNumbers`, we can double or square the numbers in the `numbers` list, demonstrating the use of higher-order functions to abstract behavior and create more flexible code.

## Futures
> Futures in Scala represent a computation that will be completed at some point in the future. 
> They are used to perform asynchronous programming, where you can execute tasks concurrently without blocking the main thread. 
> Futures allow you to perform non-blocking operations, making it ideal for tasks that may take some time to complete, such as network requests or file I/O.

Using Futures in Scala for asynchronous tasks:
```scala
import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

// Simulate an asynchronous task that returns a result after a delay
def performTask(): Future[String] = {
  Future {
    Thread.sleep(2000) // Simulating a delay of 2 seconds
    "Task completed successfully!"
  }
}

// Callbacks for handling the result of the Future
def onSuccess(result: String): Unit = {
  println(s"Task result: $result")
}

def onFailure(ex: Throwable): Unit = {
  println(s"Task failed with exception: ${ex.getMessage}")
}

// Main program
def main(args: Array[String]): Unit = {
  val future = performTask()

  // Register callbacks for success and failure
  future.onComplete {
    case Success(result) => onSuccess(result)
    case Failure(ex) => onFailure(ex)
  }

  println("Waiting for the task to complete...")
  Thread.sleep(3000) // Simulating some other work while waiting for the Future to complete
}
```

In this example, the `performTask` function returns a Future that simulates an asynchronous task with a 2-second delay. 
We use the `Future` constructor to wrap the task’s code inside a Future. 
The `onComplete` method is used to register callbacks for handling the result of the Future. 
The `onSuccess` callback is invoked if the Future completes successfully, and the `onFailure` callback is called if the Future fails with an exception.

In the `main` method, we call `performTask`, and while the Future is executing, we simulate some other work with a 3-second delay. 
This demonstrates the non-blocking behavior of Futures, allowing other work to be done while waiting for the Future to complete.

## Type Classes
> Type classes in Scala are a way of achieving ad-hoc polymorphism, where different data types can exhibit similar behavior without being related through a common inheritance hierarchy. 
> Type classes enable adding behavior to existing classes without modifying their source code.

Role of type classes in providing ad-hoc polymorphism:
1) **Decoupling:** Type classes allow adding functionality to a type without modifying it, promoting decoupling and avoiding the need for a common base class.
2) **Open for Extension:** Type classes are open for extension, meaning new behaviors can be added for existing types without changing their definitions.
3) **Generic Programming:** Type classes enable writing generic code that works with any type that satisfies the type class constraints, enhancing code reusability.

### Example

```scala
// Type class definition for ordering
trait MyOrdering[A] {
  def compare(x: A, y: A): Int
}

// Type class instance for ordering integers
implicit object IntOrdering extends MyOrdering[Int] {
  def compare(x: Int, y: Int): Int = x - y
}

// Type class instance for ordering strings
implicit object StringOrdering extends MyOrdering[String] {
  def compare(x: String, y: String): Int = x.compareTo(y)
}

// Generic function using type class
def findMax[A](list: List[A])(implicit ord: MyOrdering[A]): A = {
  list.reduceLeft((x, y) => if (ord.compare(x, y) > 0) x else y)
}

val intList = List(5, 2, 8, 3)
val stringList = List("apple", "orange", "banana")

val maxInt = findMax(intList)
val maxString = findMax(stringList)

println(s"Max integer: $maxInt")
println(s"Max string: $maxString")
```

In this example, we define a type class `MyOrdering`, representing a comparison behavior. 
We create type class instances `IntOrdering` and `StringOrdering` to provide ordering capabilities for integers and strings, respectively.

The `findMax` function is a generic function that can find the maximum element in a list of any type that has a corresponding `MyOrdering` instance. 
By using implicit parameters, Scala can automatically look up the appropriate type class instance for the given data type.

The `findMax` function demonstrates how type classes enable generic programming by providing ad-hoc polymorphism, allowing the same function to work with different types as long as they satisfy the type class constraints.

## Implicits
> Implicits in Scala are a powerful feature used for automatic type conversions, parameter injection, and resolution of missing or default values. 
> Implicits allow the compiler to insert code automatically, making the code more concise and expressive.

Role of implicits in automatic type conversions:
1) **Type Coercion:** Implicits enable automatic type conversions between different data types when required, allowing smoother interoperability between different parts of the code.
2) **Implicit Parameters:** Implicits can be used to define parameters that are automatically passed to functions without explicitly providing them, making function calls more concise.
3) **Companion Object Extensions:** Implicits can be used to extend the functionality of a class by defining implicit methods in its companion object, making it possible to call those methods as if they were part of the class.

### Example

```scala
// Define a class representing distances in meters
case class Meter(value: Double)

// Define an implicit conversion from meters to centimeters
implicit def meterToCentimeter(m: Meter): Centimeter = Centimeter(m.value * 100)

// Define a class representing distances in centimeters
case class Centimeter(value: Double)

// Function that accepts Centimeter as a parameter
def printDistanceInCentimeter(cm: Centimeter): Unit = {
  println(s"Distance: ${cm.value} centimeters")
}

val distanceInMeters = Meter(5.5)
printDistanceInCentimeter(distanceInMeters) // The implicit conversion is automatically applied
```

In this example, we define two case classes `Meter` and `Centimeter`, representing distances in meters and centimeters, respectively. 
We define an implicit conversion from `Meter` to `Centimeter` by defining an implicit method `meterToCentimeter`. 
This conversion allows Scala to automatically convert a `Meter` object to a `Centimeter` object when needed.

The `printDistanceInCentimeter` function accepts a `Centimeter` object as a parameter. 
When we call this function with a `Meter` object, Scala automatically applies the implicit conversion defined in the companion object of `Meter`, converting it to `Centimeter` before passing it to the function.

## Algebraic Data Types (ADTs)
> Algebraic Data Types (ADTs) in Scala are data types formed by combining other data types using algebraic operations.
> ADTs are crucial in functional programming as they enable modeling complex data structures with precision and type safety.

Significance of Algebraic Data Types in functional programming:
1) **Data Modeling:** ADTs allow developers to model data structures with precision, clarity, and safety. They provide a clear definition of all possible cases or states that a data type can have.
2) **Pattern Matching:** ADTs work seamlessly with pattern matching, enabling concise and robust code for handling different cases.
3) **Immutability:** ADTs are usually designed to be immutable, promoting safe and concurrent programming.

### Example

```scala
sealed trait Shape

final case class Circle(radius: Double) extends Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Square(side: Double) extends Shape

def area(shape: Shape): Double = shape match {
  case Circle(r) => math.Pi * r * r
  case Rectangle(w, h) => w * h
  case Square(s) => s * s
}

val circle = Circle(2.5)
val rectangle = Rectangle(3.0, 4.0)
val square = Square(2.0)

println(s"Area of Circle: ${area(circle)}")
println(s"Area of Rectangle: ${area(rectangle)}")
println(s"Area of Square: ${area(square)}")
```

In this example, we define an ADT `Shape`, representing different geometric shapes. 
The ADT is defined using the `sealed trait` keyword, which ensures that all possible shapes are defined within the same file.

We create three case classes `Circle`, `Rectangle`, and `Square`, representing specific shapes. 
Each case class extends the `Shape` trait, and each shape has its own specific properties.

The `area` function is a pattern-matching function that calculates the area of each shape based on its type. 
Pattern matching allows us to handle different cases of the ADT efficiently.

When we call `area` with different instances of `Shape`, Scala automatically determines the appropriate case and calculates the area of the specific shape, demonstrating the power of ADTs and pattern matching in functional programming.

## Monads
> Monads in Scala are a design pattern used to represent sequences of computations and manage side effects in a functional and composable way. 
> Monads encapsulate values and computations into a container, providing a set of operations to chain, transform, and combine computations.

Significance of Monads in functional programming:
1) **Composition:** Monads enable sequential composition of computations, providing a clean and modular approach to manage complex sequences of operations.
2) **Error Handling:** Monads can handle error scenarios elegantly by providing mechanisms to propagate errors through the computation chain without the need for explicit error handling.
3) **Side Effects:** Monads help manage side effects in functional programming by encapsulating the side effects within the monadic context.

### Example

```scala
// Option Monad for handling optional values
object OptionMonad {
  def apply[A](value: A): Option[A] = Some(value)

  implicit class OptionOps[A](option: Option[A]) {
    def flatMap[B](f: A => Option[B]): Option[B] = option match {
      case Some(value) => f(value)
      case None => None
    }

    def map[B](f: A => B): Option[B] = option match {
      case Some(value) => Option(f(value))
      case None => None
    }
  }
}

import OptionMonad._

def divide(a: Int, b: Int): Option[Double] =
  if (b != 0) Option(a.toDouble / b) else None

val result1 = divide(10, 2).flatMap(r => divide(r.toInt, 3))
val result2 = divide(10, 0).flatMap(r => divide(r.toInt, 3))

println(s"Result 1: $result1")
println(s"Result 2: $result2")
```

In this example, we define a simple Monad for handling optional values, called `OptionMonad`. 
The `OptionOps` class provides `flatMap` and `map` operations, which are essential operations for any Monad.

The `divide` function returns an `Option[Double]` representing the result of the division. 
If the denominator is not zero, it returns the result wrapped in a `Some`, otherwise, it returns `None`.

We use the Monad operations `flatMap` to chain two division operations, where the second operation depends on the result of the first one. 
The `map` operation is used to perform a computation on the result without unwrapping it from the Monad.

The use of Monads in this example demonstrates how to handle optional values and chain operations gracefully, while also handling error scenarios. 
Monads provide a powerful abstraction for working with various computation contexts in a consistent and composable way.