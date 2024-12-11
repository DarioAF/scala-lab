package practice.numbers

import scala.annotation.tailrec

object BasicOps {
  implicit class RRichInt(n: Int) {
    // complexity: O(sqrt(N))
    def isPrime: Boolean = {
      @tailrec def tailrec(i: Int): Boolean = {
        if (i > Math.sqrt(Math.abs(n))) true
        else n % i != 0 && tailrec(i + 1)
      }

      if (n == 0 || n == 1 || n == -1) false
      else tailrec(2)
    }

    // complexity: O(sqrt(N)), can be as low as O(log(N))
    def decompose: List[Int] = {
      assert(n > 0)

      @tailrec def tailrec(i: Int, divisor: Int, acc: List[Int]): List[Int] = {
        if (divisor > Math.sqrt(i)) i :: acc
        else if (i % divisor == 0) tailrec(i / divisor, divisor, divisor :: acc)
        else tailrec(i, divisor + 1, acc)
      }

      tailrec(n, 2, List.empty)
    }
  }
}

object NumberProblems extends App {
  import BasicOps._

  val testPrime = (n: Int) => println(s"$n isPrime? -> ${n.isPrime}")
  val testDecompose = (n: Int) => println(s"decompose $n -> ${n.decompose}")

  testPrime(13)
  testPrime(27)
  testDecompose(13)
  testDecompose(27)
}
